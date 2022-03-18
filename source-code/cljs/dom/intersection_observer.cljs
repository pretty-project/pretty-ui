
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.intersection-observer)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn intersection-observer
  ; @param (string) element-id
  ; @param (function) callback-f
  ;
  ; @usage
  ;  (dom/intersection-observer "my-element" (fn [] ...))
  ;
  ; @return (object)
  [_ callback-f]
  (letfn [(f [%] (if (-> % (aget 0) .-isIntersecting)
                     (callback-f)))]
         (js/IntersectionObserver. f {})))

(defn setup-intersection-observer!
  ; @param (string) element-id
  ; @param (function) callback-f
  ;
  ; @usage
  ;  (dom/setup-intersection-observer! "my-element" (fn [] ...))
  [element-id callback-f]
  (let [observer         (intersection-observer element-id callback-f)
        observer-element (.getElementById js/document element-id)]
       (.observe observer observer-element)))
