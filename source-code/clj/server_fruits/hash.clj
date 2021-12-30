
(ns server-fruits.hash
    (:require [buddy.core.mac    :as mac]
              [buddy.core.codecs :as codecs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sha256
  ; @param (string) n
  ; @param (string) secret-key
  ;
  ; @usage
  ;  (hash/sha256 "My text" "my-secret-key")
  ;
  ; @return (string)
  [n secret-key]
  (-> n (mac/hash {:key secret-key :alg :hmac+sha256})
        (codecs/bytes->hex)))
