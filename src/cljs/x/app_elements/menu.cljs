
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.13
; Description:
; Version: v0.6.0
; Compatibility: x4.3.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.menu
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

(defn- menu-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (map) view-props
  ;  {:always-expanded? (boolean)(opt)}
  ;
  ; @return (map)
  [menu-id {:keys [always-expanded?] :as view-props}]
  (let [filters        [:class :orientation :style]
        filtered-props (map/inherit view-props filters)]
       (engine/element-attributes menu-id filtered-props
         (if-not always-expanded? {:on-mouse-leave (engine/on-hide-function menu-id)}))))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items<-control-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (maps in vector) menu-items
  ;
  ; @return (map)
  [menu-id menu-items]
  (reduce (fn [menu-items {:keys [content-id] :as item-props}]
              (let [set-function (engine/on-show-function menu-id content-id)
                    item-props   (assoc item-props :on-mouse-over set-function)]
                   (vector/conj-item menu-items item-props)))
          (param [])
          (param menu-items)))

(defn- view-props->bar-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (map) view-props
  ;  {:menu-items (maps in vector)}
  ;
  ; @return (map)
  ;  {:menu-items (maps in vector)}
  [menu-id {:keys [menu-items] :as view-props}]
  (let [filters [:font-size :horizontal-align :layout :orientation :selected-content-id]
        filtered-props (map/inherit view-props filters)]
       (merge (menu-bar/bar-props-prototype filtered-props)
              {:menu-items (menu-items<-control-events menu-id menu-items)})))

(defn- view-props->surface-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (map) view-props
  ;
  ; @return (map)
  ;  {:position (keyword)}
  [_ view-props]
  (let [filters [:surface-items :selected-content-id]
        filtered-props (map/inherit view-props filters)]
       (merge (content-surface/surface-props-prototype filtered-props)
              {:position :absolute})))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) menu-props
  ;  {:always-expanded? (boolean)(opt)
  ;   :menu-items (maps in vector)}
  ;
  ; @return (map)
  ;  {:bar-id (keyword)
  ;   :orientation (keyword)
  ;   :container-stretch-orientation (keyword)
  ;   :selected-content-id (keyword)
  ;   :surface-id (keyword)}
  [{:keys [always-expanded? menu-items] :as menu-props}]
  (merge {:bar-id     (random/generate-keyword)
          :surface-id (random/generate-keyword)
          :orientation :horizontal}
         (if always-expanded?
             ; XXX#4339 Default selected content-id
             {:selected-content-id (engine/visible-items->first-content-id menu-items)})
         (param menu-props)
         {:container-stretch-orientation :horizontal}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ;
  ; @return (map)
  [db [_ menu-id]]
  (merge (r engine/get-element-view-props db menu-id)
         (r engine/get-visible-view-props db menu-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) menu-id
  ; @param (map) view-props
  ;  {:bar-id (keyword)
  ;   :surface-id (keyword)}
  ;
  ; @return (hiccup)
  [menu-id {:keys [bar-id surface-id] :as view-props}]
  (let [bar-props     (view-props->bar-props     menu-id view-props)
        surface-props (view-props->surface-props menu-id view-props)]
       [:div.x-menu (menu-attributes menu-id view-props)
                    [menu-bar bar-id bar-props]
                    [content-surface surface-id surface-props]]))

(defn view
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ;  {:always-expanded? (boolean)(opt)
  ;    Default: false
  ;   :class (string or vector)(opt)
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
  ;   :menu-items (maps in vector)
  ;    [{:content-id (keyword)
  ;      :href (string)(opt)
  ;       XXX#7004
  ;      :icon (keyword)(opt) Material icon class
  ;      :label (metamorphic-content)
  ;      :on-click (metamorphic-event)(opt)}]
  ;   :orientation (keyword)(opt)
  ;    :horizontal, vertical
  ;    Default: :horizontal
  ;   :style (map)(opt)
  ;   :surface-items (maps in vector)
  ;    [{:content (metamorphic-content)
  ;      :content-id (keyword)
  ;      :content-props (map)(opt)
  ;      :subscriber (subscription vector)(opt)}]}
  ;
  ; @usage
  ;  [elements/menu {...}]
  ;
  ; @usage
  ;  [elements/menu :my-menu {...}]
  ;
  ; @usage
  ;  [elements/menu :my-menu {:menu-items [{:content-id :item-1
  ;                                         :label      "Item #1"}
  ;                                        {:content-id :item-2
  ;                                         :label      "Item #2"}]
  ;                           :surface-items [{:content    "Content #1"
  ;                                            :content-id :item-1}
  ;                                           {:content    "Content #2"
  ;                                            :content-id :item-2}]}]
  ;
  ; @return (component)
  ([menu-props]
   [view nil menu-props])

  ([menu-id menu-props]
   (let [menu-id    (a/id   menu-id)
         menu-props (a/prot menu-props menu-props-prototype)]
        [engine/container menu-id
          {:base-props menu-props
           :component  menu
           :subscriber [::get-view-props menu-id]}])))
