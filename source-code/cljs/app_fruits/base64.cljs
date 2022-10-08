
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.base64)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn wrap
  ; @param (string) base64
  ; @param (string) mime-type
  ;
  ; @example
  ;  (base64/wrap "..." "application/pdf")
  ;  =>
  ;  "data:application/pdf;base64,..."
  ;
  ; @example
  ;  (base64/wrap nil "application/pdf")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [base64 mime-type]
  (if base64 (str "data:"mime-type";base64,"base64)))

(defn to-blob
  ; @param (string) base64
  ; @param (string) mime-type
  ;
  ; @usage
  ;  (base64/to-blob "..." "application/pdf")
  ;
  ; @return (object)
  [base64 mime-type]
  (let [binary-string (.atob js/window base64)
        binary-length (.-length binary-string)
        integer-array (js/Uint8Array. binary-length)]
       (doseq [i (range binary-length)]
              (aset integer-array i (.charCodeAt binary-string i)))
       (js/Blob. (clj->js [integer-array])
                 (clj->js {:type mime-type}))))
