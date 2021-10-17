
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.8.2
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-ui.element     :as element]
              [x.app-ui.renderer    :refer [view] :rename {view renderer}]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [header-id header-props]
  (element/element-attributes :header header-id header-props
                              {:data-nosnippet true}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) header-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :position (keyword)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)}
  [header-props]
  (merge {:hide-animated?   false
          :position         :fixed
          :reveal-animated? false
          :update-animated? false}
         (param header-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/remove-header!
  [:x.app-ui/empty-renderer! :header])

(a/reg-event-fx
  :x.app-ui/set-header!
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:content (metamorphic-content)(opt)
  ;    XXX#8711
  ;   :content-props (map)(opt)
  ;    XXX#8711
  ;   :hide-animated? (boolean)(opt)
  ;    Default: false
  ;   :position (keyword)(opt)
  ;    :fixed, :relative
  ;    Default: :fixed
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: false
  ;   :subscriber (subscription vector)(opt)
  ;    XXX#8711
  ;   :update-animated? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [:x.app-ui/set-header! {...}]
  ;
  ; @usage
  ;  [:x.app-ui/set-header! :my-header {...}]
  (fn [_ event-vector]
      (let [header-id    (a/event-vector->second-id   event-vector)
            header-props (a/event-vector->first-props event-vector)
            header-props (a/prot header-props header-props-prototype)]
           [:x.app-ui/render-element! :header header-id header-props])))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (hiccup)
  [header-id header-props]
  [:div.x-app-header--element--content
    [element/element-content header-id header-props]])

(defn- header-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (hiccup)
  [header-id header-props]
  [:div (header-attributes header-id header-props)
        [header-content    header-id header-props]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :header {:element               #'header-element
                     :max-elements-rendered 1
                     :queue-behavior        :push
                     :rerender-same?        false}])
