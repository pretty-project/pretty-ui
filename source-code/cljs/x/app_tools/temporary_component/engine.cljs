
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.temporary-component.engine
    (:require [app-fruits.dom                         :as dom]
              [app-fruits.reagent                     :as reagent]
              [x.app-tools.temporary-component.config :as temporary-component.config]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-environment-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (DOM-element)
  []
  (dom/get-element-by-id temporary-component.config/ENVIRONMENT-ELEMENT-ID))

(defn- create-temporary-container!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (DOM-element)
  []
  (let [environment-element (get-environment-element)
        temporary-container (dom/create-element! "DIV")]
       (dom/set-element-id! temporary-container "x-temporary-component")
       (dom/append-element! environment-element temporary-container)))

(defn- remove-temporary-container!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [temporary-container (dom/get-element-by-id "x-temporary-component")]
          (let [environment-element (get-environment-element)]
               (dom/remove-child! environment-element temporary-container))))



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
   (remove-temporary-container!)
   (create-temporary-container!)
   (let [temporary-container (dom/get-element-by-id "x-temporary-component")]
        (reagent/render component temporary-container)))

  ([component render-callback]
   (remove-temporary-container!)
   (create-temporary-container!)
   (let [temporary-container (dom/get-element-by-id "x-temporary-component")]
        (reagent/render component temporary-container render-callback))))

(defn remove-temporary-component!
  ; @usage
  ;  (tools/remove-temporary-component!)
  []
  (remove-temporary-container!))
