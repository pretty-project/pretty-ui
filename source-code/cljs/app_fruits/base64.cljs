
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.base64
    (:require [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn wrap
  ; @param (string) base64
  ; @param (string) mime-type
  ;
  ; @example
  ;  (wrap "..." "application/pdf")
  ;  =>
  ;  "data:application/pdf;base64,..."
  ;
  ; @example
  ;  (wrap "" "application/pdf")
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (wrap nil "application/pdf")
  ;  =>
  ;  nil
  ;
  ; @return (string)
  [base64 mime-type]
  (if (string/nonempty? base64)
      (str "data:"mime-type";base64,"base64)))

(defn to-blob
  ; @param (string) base64
  ; @param (string) mime-type
  ;
  ; @usage
  ;  (to-blob "..." "application/pdf")
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
