
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.29
; Description:
; Version: v1.0.2
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.background
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.logical   :refer [nonfalse?]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-ui.element     :as element]
              [x.app-ui.renderer    :as renderer :refer [view] :rename {view renderer}]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- background-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) background-id
  ; @param (map) background-props
  ;
  ; @return (map)
  [background-id background-props]
  (element/element-attributes :background background-id background-props
                              {:data-nosnippet true}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- background-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) background-props
  ;  {:fixed? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :layout (keyword)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)}
  [{:keys [fixed?] :as background-props}]
  (merge {:hide-animated?   true
          :reveal-animated? true
          :update-animated? false}
         (if (nonfalse? fixed?) {:layout :fixed})
         (param background-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/set-background!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) background-id
  ; @param (map) background-props
  ;  {:content (metamorphic-content)
  ;    XXX#8711
  ;   :fixed? (boolean)(opt)
  ;    Default: true
  ;   :hide-animated? (boolean)(opt)
  ;    Default: true
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: true
  ;   :update-animated? (boolean)(opt)
  ;    Default: false}
  (fn [_ event-vector]
      (let [background-id    (a/event-vector->second-id   event-vector)
            background-props (a/event-vector->first-props event-vector)
            background-props (a/prot background-props background-props-prototype)]
           [:x.app-ui/render-element! :background background-id background-props])))

(a/reg-event-fx
  :x.app-ui/remove-background!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [background-id (r renderer/get-upper-visible-element-id db :background)]
           [:x.app-ui/destroy-element! :background background-id])))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- background-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) background-id
  ; @param (map) background-props
  ;
  ; @return (hiccup)
  [background-id background-props]
  (let [context-props (components/extended-props->content-props background-props)]
       [:div (background-attributes background-id background-props)
             [components/content context-props]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :background {:element               #'background-element
                         :max-elements-rendered 1
                         :queue-behavior        :push
                         :rerender-same?        false}])
