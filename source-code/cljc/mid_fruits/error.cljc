
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.03
; Description:
; Version: v0.4.4



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
  #?(:cljs (try (apply f abc) (catch :default  e (str e)))
     :clj  (try (apply f abc) (catch Exception e (str e)))))

(defn throw!
  ; @param (string) e
  ;
  ; @usage
  ;  (error/throw! "Something went wrong ...")
  ;
  ; @return (?)
  [e]
  #?(:cljs (throw (js/Error.  e))
     :clj  (throw (Exception. e))))
