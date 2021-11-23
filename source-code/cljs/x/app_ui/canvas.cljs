
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.07
; Description:
; Version: v1.0.8
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.canvas
   (:require [mid-fruits.candy  :refer [param]]
             [x.app-core.api    :as a]
             [x.app-ui.element  :as element]
             [x.app-ui.renderer :rename {component renderer}]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (integer)
(def MAX-CANVAS-ITEMS-RENDERED 16)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) item-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)}
  [item-props]
  (merge {:hide-animated?   true
          :reveal-animated? true
          :update-animated? false}
         (param item-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/add-item!
  ; @param (keyword)(opt) item-id
  ; @param (map) item-props
  ;  {:content (metamorphic-content)
  ;    XXX#8711
  ;   :content-props (map)(opt)
  ;    XXX#8711
  ;   :hide-animated? (boolean)(opt)
  ;    Default: true
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: true
  ;   :subscriber (subscription vector)(opt)
  ;    XXX#8711
  ;   :update-animated? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [:x.app-ui/add-item! {...}]
  ;
  ; @usage
  ;  [:x.app-ui/add-item! :my-item {...}]
  (fn [_ event-vector]
      (let [item-id    (a/event-vector->second-id   event-vector)
            item-props (a/event-vector->first-props event-vector)
            item-props (a/prot item-props item-props-prototype)]
           [:x.app-ui/request-rendering-element! :canvas item-id item-props])))

(a/reg-event-fx
  :x.app-ui/remove-item!
  ; @param (keyword) item-id
  (fn [_ [_ item-id]]
      [:x.app-ui/destroy-element! :canvas item-id]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- canvas-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ;
  ; @return (hiccup)
  [item-id item-props]
  [:div (element/element-attributes :canvas item-id item-props
                                    {:data-nosnippet true})
        [element/element-content item-id item-props]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :canvas {:element              #'canvas-item
                     :max-elements-rendered MAX-CANVAS-ITEMS-RENDERED
                     :queue-behavior       :ignore
                     :rerender-same?       false}])
