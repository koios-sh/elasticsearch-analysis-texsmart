#!/usr/bin/python
from ctypes import *
import os
import sys

my_dir_path = os.path.dirname(os.path.realpath(__file__)) + '/'
dll_name = 'libtencent_ai_texsmart.so'
if sys.platform.startswith("win"):
    dll_name = 'tencent_ai_texsmart.dll'
elif sys.platform == "cygwin":
    dll_name = "tencent_ai_texsmart.dll"
lib = cdll.LoadLibrary(my_dir_path + dll_name)

class NluToken(Structure):
    _fields_ = [
        ('str', c_wchar_p),
        ('offset', c_uint32),
        ('type', c_uint32),
    ]

class NluTerm(Structure):
    _fields_ = [
        ('str', c_wchar_p),
        ('offset', c_uint32),
        ('len', c_uint32),
        ('start_token', c_uint32),
        ('token_count', c_uint32),
        ('tag', c_wchar_p),
        ('tag_id', c_uint32),
    ]

class NluEntityType(Structure):
    _fields_ = [
        ('name', c_wchar_p),
        ('i18n', c_wchar_p),
        ('flag', c_uint32),
        ('path', c_wchar_p),
    ]

class NluEntityTypeArray(Structure):
    _fields_ = [
        ('size', c_uint32),
        ('items', POINTER(NluEntityType)),
    ]

class NluEntity(Structure):
    _fields_ = [
        ('str', c_wchar_p),
        ('offset', c_uint32),
        ('len', c_uint32),
        ('start_token', c_uint32),
        ('token_count', c_uint32),
        ('type', NluEntityType),
        ('alt_types', NluEntityTypeArray),
        ('meaning', c_wchar_p),
    ]

class _NluTokenArray(Structure):
    _fields_ = [
        ('size', c_uint32),
        ('items', POINTER(NluToken)),
    ]

class _NluTermArray(Structure):
    _fields_ = [
        ('size', c_uint32),
        ('items', POINTER(NluTerm)),
    ]

class _NluEntityArray(Structure):
    _fields_ = [
        ('size', c_uint32),
        ('items', POINTER(NluEntity)),
    ]

lib.Nlu_CreateEngine.restype = c_void_p
lib.Nlu_CreateEngine.argtypes = [c_char_p, c_int]
lib.Nlu_DestroyEngine.argtypes = [c_void_p]
lib.Nlu_ParseText.restype = c_void_p
lib.Nlu_ParseText.argtypes = [c_void_p, c_wchar_p, c_int]
lib.Nlu_ParseTextExt.restype = c_void_p
lib.Nlu_ParseTextExt.argtypes = [c_void_p, c_wchar_p, c_int, c_wchar_p]
lib.Nlu_DestroyOutput.argtypes = [c_void_p]
lib.Nlu_GetNormText.restype = c_wchar_p
lib.Nlu_GetNormText.argtypes = [c_void_p, POINTER(c_int)]
lib.Nlu_GetTokens.restype = _NluTokenArray
lib.Nlu_GetTokens.argtypes = [c_void_p]
lib.Nlu_GetWords.restype = _NluTermArray
lib.Nlu_GetWords.argtypes = [c_void_p]
lib.Nlu_GetPhrases.restype = _NluTermArray
lib.Nlu_GetPhrases.argtypes = [c_void_p]
lib.Nlu_GetEntities.restype = _NluEntityArray
lib.Nlu_GetEntities.argtypes = [c_void_p]

class NluOutput(object):
    def __init__(self, ptr):
        self.obj = ptr
    def __del__(self):
        if(self.obj is not None):
            lib.Nlu_DestroyOutput(self.obj)
            self.obj = None
    def norm_text(self):
        ret = lib.Nlu_GetNormText(self.obj, None)
        return ret
    def tokens(self):
        arr = []
        item_list =  lib.Nlu_GetTokens(self.obj)
        for idx in range(item_list.size):
            arr.append(item_list.items[idx])
        return arr
    def words(self):
        arr = []
        item_list = lib.Nlu_GetWords(self.obj)
        for idx in range(item_list.size):
            arr.append(item_list.items[idx])
        return arr
    def phrases(self):
        arr = []
        item_list = lib.Nlu_GetPhrases(self.obj)
        for idx in range(item_list.size):
            arr.append(item_list.items[idx])
        return arr
    def entities(self):
        arr = []
        #count = lib.Nlu_GetEntityCount(self.obj)
        #for idx in range(count):
        #    arr.append(lib.Nlu_GetEntity(slef.obj, idx))
        item_list = lib.Nlu_GetEntities(self.obj)
        for idx in range(item_list.size):
            arr.append(item_list.items[idx])
        return arr

class NluEngine(object):
    def __init__(self, data_dir, worker_count):
        self.obj = lib.Nlu_CreateEngine(data_dir.encode('utf-8'), worker_count)
    def __del__(self):
        if self.obj is not None:
            lib.Nlu_DestroyEngine(self.obj)
            self.obj = None
    def parse_text(self, input_str):
        output_handle = lib.Nlu_ParseText(self.obj, c_wchar_p(input_str), len(input_str))
        return NluOutput(output_handle)
    def parse_text_ext(self, input_str, options_str):
        output_handle = lib.Nlu_ParseTextExt(self.obj, c_wchar_p(input_str), len(input_str), c_wchar_p(options_str))
        return NluOutput(output_handle)
