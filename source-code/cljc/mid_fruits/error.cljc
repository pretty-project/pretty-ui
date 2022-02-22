
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.03
; Description:
; Version: v0.4.8



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
  #?(:clj  (try (apply f abc) (catch Exception e (str e)))
     :cljs (try (apply f abc) (catch :default  e (str e)))))

(defn throw!
  ; @param (string) e
  ;
  ; @usage
  ;  (error/throw! "Something went wrong ...")
  ;
  ; @return (?)
  [e]
  #?(:clj  (throw (Exception. e))
     :cljs (throw (js/Error.  e))))
