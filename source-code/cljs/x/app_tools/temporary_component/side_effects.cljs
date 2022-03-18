
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.temporary-component.side-effects
    (:require [app-fruits.reagent                      :as reagent]
              [dom.api                                 :as dom]
              [x.app-tools.temporary-component.helpers :as temporary-component.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-temporary-component!
  ; @param (component) component
  ; @param (function)(opt) render-callback
  ;
  ; @usage W/o callback
  ;  (defn my-component [])
  ;  (tools/append-temporary-component! [my-component])
  ;
  ; @usage W/ callback
  ;  (defn my-button [] [:a {:href "foo/bar"}])
  ;  (defn click-my-button! [] ...)
  ;  (tools/append-temporary-component! [my-button] click-my-button!)
  ([component]
   (temporary-component.helpers/remove-temporary-container!)
   (temporary-component.helpers/create-temporary-container!)
   (let [temporary-container (dom/get-element-by-id "x-temporary-component")]
        (reagent/render component temporary-container)))

  ([component render-callback]
   (temporary-component.helpers/remove-temporary-container!)
   (temporary-component.helpers/create-temporary-container!)
   (let [temporary-container (dom/get-element-by-id "x-temporary-component")]
        (reagent/render component temporary-container render-callback))))

(defn remove-temporary-component!
  ; @usage
  ;  (tools/remove-temporary-component!)
  []
  (temporary-component.helpers/remove-temporary-container!))
