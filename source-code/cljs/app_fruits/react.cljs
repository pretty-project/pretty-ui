
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.react
    (:require ["react" :as react]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; react
(def use-effect react/useEffect)
(def use-ref    react/useRef)
(def use-state  react/useState)



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(def REFERENCES (atom {}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-reference!
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  [:div {:ref (react/set-reference! :my-element)}]
  ;
  ; @return (function)
  [element-id]
  #(swap! REFERENCES assoc element-id %))

(defn get-reference
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  (react/get-reference :my-element)
  ;
  ; @return (function)
  [element-id]
  (get @REFERENCES element-id))
