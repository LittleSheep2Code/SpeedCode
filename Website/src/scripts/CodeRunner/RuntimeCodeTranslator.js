export default class Translator {

  static go(code_mirror_id) {
    switch(code_mirror_id) {
      case "javascript": return "NODE_JS"
      case "x-python": return "PYTHON_3"
      case "x-csrc": return "C_GCC_9"
      case "x-c++rc": return "CPP_GPP_9"
      case "x-csharp": return "C_SHARP"

      default: console.log("[WARN]: CANNOT FOUND TARGET RUNTIME: " + code_mirror_id)
    }
  }

  static back(runtime_id) {
    switch(runtime_id) {
      case "NODE_JS": return "javascript"
      case "PYTHON_3": case "PYTHON_2": return "x-python"
      case "C_GCC_7": case "C_GCC_8": case "C_GCC_9": case "C_CLANG_7": return "x-csrc"
      case "CPP_GPP_7": case "CPP_GPP_8": case "CPP_GPP_9": case "CPP_CLANG_7": return "x-c++rc"
      case "C_CSHARP": return "x-csharp"
    }
  }
}
