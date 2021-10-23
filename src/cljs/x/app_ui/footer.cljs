
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.8.2
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.footer
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-ui.element     :as element]
              [x.app-ui.renderer    :refer [view] :rename {view renderer}]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  [footer-id footer-props]
  (element/element-attributes :footer footer-id footer-props
                              {:data-nosnippet true}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) footer-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :position (keyword)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)}
  [footer-props]
  (merge {:hide-animated?   false
          :position         :relative
          :reveal-animated? false
          :update-animated? false}
         (param footer-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/remove-footer!
  [:x.app-ui/empty-renderer! :footer])

(a/reg-event-fx
  :x.app-ui/set-footer!
  ; @param (keyword)(opt) footer-id
  ; @param (map) footer-props
  ;  {:content (metamorphic-content)(opt)
  ;    XXX#8711
  ;   :content-props (map)(opt)
  ;    XXX#8711
  ;   :hide-animated? (boolean)(opt)
  ;    Default: false
  ;   :position (keyword)(opt)
  ;    :fixed, :relative
  ;    Default: :relative
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: false
  ;   :subscriber (subscription vector)(opt)
  ;    XXX#8711
  ;   :update-animated? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [:x.app-ui/set-footer! {...}]
  ;
  ; @usage
  ;  [:x.app-ui/set-footer! :my-footer {...}]
  (fn [_ event-vector]
      (let [footer-id    (a/event-vector->second-id   event-vector)
            footer-props (a/event-vector->first-props event-vector)
            footer-props (a/prot footer-props footer-props-prototype)]
           [:x.app-ui/request-rendering-element! :footer footer-id footer-props])))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  ;
  ; @return (hiccup)
  [footer-id footer-props]
  [:div.x-app-footer--element--content
    [element/element-content footer-id footer-props]])

(defn- footer-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  ;
  ; @return (hiccup)
  [footer-id footer-props]
  [:div (footer-attributes footer-id footer-props)
        [footer-content    footer-id footer-props]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :footer {:element               #'footer-element
                     :max-elements-rendered 1
                     :queue-behavior        :push
                     :rerender-same?        false}])
