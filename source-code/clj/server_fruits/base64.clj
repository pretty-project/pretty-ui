
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-fruits.base64
    (:require [clojure.data.codec.base64 :as base64]
              [server-fruits.io          :as io]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn encode
  ; @param (string) source-filepath
  ; @param (string) destination-filepath
  ;
  ; @usage
  ;  (base64/encode "my-document.pdf" "my-document.b64")
  ;
  ; @return (string)
  [source-filepath destination-filepath]
  (with-open [i (io/input-stream  source-filepath)
              o (io/output-stream destination-filepath)]
             (base64/encoding-transfer i o))
  (slurp destination-filepath))
