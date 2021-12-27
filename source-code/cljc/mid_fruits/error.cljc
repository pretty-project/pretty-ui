
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.03
; Description:
; Version: v0.3.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.error)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn try!
  ; @param (function) f
  ; @param (list of *) abc
  ;
  ; @usage
  ;  (error/try! #(do-something! "Apple"))
  ;
  ; @usage
  ;  (error/try! do-something! "Apple")
  ;
  ; @return (*)
  [f & abc]
  #?(:cljs (try (apply f abc) (catch :default  e (str "Error: " n)))
     :clj  (try (apply f abc) (catch Exception e (str "Error: " n)))))
