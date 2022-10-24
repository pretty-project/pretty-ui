
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-fruits.hash
    (:require [buddy.core.mac    :as mac]
              [buddy.core.codecs :as codecs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hmac-sha256
  ; @param (string) n
  ; @param (string) secret-key
  ;
  ; @usage
  ;  (hmac-sha256 "My text" "my-secret-key")
  ;
  ; @return (hex string)
  [n secret-key]
  (-> n (mac/hash {:key secret-key :alg :hmac+sha256})
        (codecs/bytes->hex)))
