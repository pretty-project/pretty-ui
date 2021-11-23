
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.08
; Description:
; Version: v0.4.2
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.content-surface
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;
  ; @return (map)
  ;  {:position (keyword)}
  [surface-props]
  (merge {:position :relative}
         (param surface-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  [db [_ surface-id]]
  (merge (r engine/get-element-view-props db surface-id)
         (r engine/get-visible-view-props db surface-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) view-props
  ;  {:selected-content-id (keyword)}
  ; @param (map) item-props
  ;  {:content-id (keyword)}
  ;
  ; @return (hiccup)
  [menu-id {:keys [selected-content-id]} {:keys [content-id] :as item-props}]
  (if (= selected-content-id content-id)
      [components/content item-props]))

(defn content-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [surface-id {:keys [surface-items] :as view-props}]
  [:div.x-content-surface
    (engine/element-attributes surface-id view-props)
    (reduce #(vector/conj-item %1 [surface-item surface-id view-props %2])
             [:div.x-content-surface--body]
             (param surface-items))])

(defn view
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ;  {:class (string or vector)(opt)
  ;   :position (keyword)(opt)
  ;    :absolute, :relative
  ;    Default: :absolute
  ;   :selected-content-id (keyword)(opt)
  ;    XXX#4339
  ;    Default selected content-id
  ;   :style (map)(opt)
  ;   :surface-items (maps in vector)
  ;    [{:content (metamorphic-content)
  ;      :content-props (map)(opt)
  ;      :subscriber (subscription vector)(opt)}]}
  ;
  ; @usage
  ;  [elements/content-surface {...}]
  ;
  ; @usage
  ;  [elements/content-surface :my-content-surface {...}]
  ;
  ; @return (component)
  ([surface-props]
   [view nil surface-props])

  ([surface-id surface-props]
   (let [surface-id    (a/id   surface-id)
         surface-props (a/prot surface-props surface-props-prototype)]
        [engine/stated-element surface-id
          {:component     #'content-surface
           :element-props surface-props
           :subscriber    [::get-view-props surface-id]}])))
