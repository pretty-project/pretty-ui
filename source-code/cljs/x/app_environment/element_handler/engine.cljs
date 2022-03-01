
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.element-handler.engine
    (:require [app-fruits.dom :as dom]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-disabled?
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/element-disabled? "my-element")
  ;
  ; @return (boolean)
  [element-id]
  (boolean (if-let [element (dom/get-element-by-id element-id)]
                   (dom/element-disabled? element))))

(defn element-enabled?
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/element-enabled? "my-element")
  ;
  ; @return (boolean)
  [element-id]
  (boolean (if-let [element (dom/get-element-by-id element-id)]
                   (dom/element-enabled? element))))
