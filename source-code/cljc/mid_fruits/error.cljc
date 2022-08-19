
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
  ;  (error/try! #(my-function "Apple"))
  ;
  ; @usage
  ;  (error/try! my-function "Apple")
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
