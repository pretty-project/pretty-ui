
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.13
; Description:
; Version: v0.5.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.tabs
    (:require [mid-fruits.candy               :refer [param return]]
              [mid-fruits.keyword             :as keyword]
              [mid-fruits.map                 :as map]
              [mid-fruits.random              :as random]
              [mid-fruits.vector              :as vector]
              [x.app-components.api           :as components]
              [x.app-core.api                 :as a :refer [r]]
              [x.app-elements.content-surface :as content-surface :refer [content-surface]]
              [x.app-elements.engine.api      :as engine]
              [x.app-elements.menu-bar        :as menu-bar :refer [menu-bar]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tabs-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) tabs-id
  ; @param (map) view-props
  ;
  ; @return (map)
  [tabs-id view-props]
  (let [filters        [:class :orientation :style]
        filtered-props (map/inherit view-props filters)]
       (engine/element-attributes tabs-id filtered-props)))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tab-items<-control-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) tabs-id
  ; @param (maps in vector) tab-items
  ;
  ; @return (map)
  [tabs-id tab-items]
  (reduce (fn [tab-items {:keys [content-id] :as item-props}]
              (let [set-function (engine/on-show-function tabs-id content-id)
                    item-props   (assoc item-props :on-click set-function)]
                   (vector/conj-item tab-items item-props)))
          (param [])
          (param tab-items)))

(defn- view-props->bar-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) tabs-id
  ; @param (map) view-props
  ;  {:tab-items (maps in vector)}
  ;
  ; @return (map)
  ;  {:menu-items (maps in vector)}
  [tabs-id {:keys [tab-items] :as view-props}]
  (let [filters [:font-size :horizontal-align :layout :orientation :selected-content-id]
        filtered-props (map/inherit view-props filters)]
       (merge (menu-bar/bar-props-prototype filtered-props)
              {:menu-items (tab-items<-control-events tabs-id tab-items)})))

(defn- view-props->surface-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) tabs-id
  ; @param (map) view-props
  ;
  ; @return (map)
  ;  {:position (keyword)}
  [_ view-props]
  (let [filters [:surface-items :selected-content-id]
        filtered-props (map/inherit view-props filters)]
       (merge (content-surface/surface-props-prototype filtered-props)
              {:position :relative})))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tabs-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) tabs-props
  ;  {:tab-items (maps in vector)}
  ;
  ; @return (map)
  ;  {:bar-id (keyword)
  ;   :container-stretch-orientation (keyword)
  ;   :orientation (keyword)
  ;   :selected-content-id (keyword)
  ;   :surface-id (keyword)}
  [{:keys [tab-items] :as tabs-props}]
  (merge {:bar-id     (random/generate-keyword)
          :surface-id (random/generate-keyword)
          ; XXX#4339 Default selected content-id
          :selected-content-id (engine/visible-items->first-content-id tab-items)
          :orientation :horizontal}
         (param tabs-props)
         {:container-stretch-orientation :horizontal}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) tabs-id
  ;
  ; @return (map)
  [db [_ tabs-id]]
  (merge (r engine/get-element-view-props db tabs-id)
         (r engine/get-visible-view-props db tabs-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tabs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) tabs-id
  ; @param (map) view-props
  ;  {:bar-id (keyword)
  ;   :surface-id (keyword)}
  ;
  ; @return (hiccup)
  [tabs-id {:keys [bar-id surface-id] :as view-props}]
  (let [bar-props     (view-props->bar-props     tabs-id view-props)
        surface-props (view-props->surface-props tabs-id view-props)]
       [:div.x-tabs (tabs-attributes tabs-id view-props)
                    [menu-bar bar-id bar-props]
                    [content-surface surface-id surface-props]]))

(defn view
  ; @param (keyword)(opt) tabs-id
  ; @param (map) tabs-props
  ;  {:class (string or vector)(opt)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;    (XXX#5406 overflow-x: scroll & {:horizontal-align :space-between} nem lehetséges)
  ;    Only w/ {:orientation :horizontal}
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :orientation (keyword)(opt)
  ;    :horizontal, vertical
  ;    Default: :horizontal
  ;   :style (map)(opt)
  ;   :surface-items (maps in vector)
  ;    [{:content (metamorphic-content)
  ;      :content-id (keyword)
  ;      :content-props (map)(opt)
  ;      :subscriber (subscription vector)(opt)}]
  ;   :tab-items (maps in vector)
  ;    [{:content-id (keyword)
  ;      :icon (keyword)(opt) Material icon class
  ;      :label (metamorphic-content)
  ;      :on-click (metamorphic-event)(opt)}]}
  ;
  ; @usage
  ;  [elements/tabs {...}]
  ;
  ; @usage
  ;  [elements/tabs :my-tabs {...}]
  ;
  ; @return (component)
  ([tabs-props]
   [view nil tabs-props])

  ([tabs-id tabs-props]
   (let [tabs-id    (a/id   tabs-id)
         tabs-props (a/prot tabs-props tabs-props-prototype)]
        [engine/container tabs-id
          {:base-props tabs-props
           :component  tabs
           :subscriber [::get-view-props tabs-id]}])))
