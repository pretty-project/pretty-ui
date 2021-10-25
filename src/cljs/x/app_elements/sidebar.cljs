
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.25
; Description:
; Version: v0.4.8
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.sidebar
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ;  {:container-floating? (boolean)
  ;   :container-position (keyword)
  ;   :container-stretch-orientation (keyword)}
  [sidebar-props]
  (merge (param sidebar-props)
         {:container-floating?           true
          :container-position            :tr
          :container-stretch-orientation :vertical}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ;
  ; @return (map)
  [db [_ sidebar-id]]
  (r engine/get-element-view-props db sidebar-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) view-props
  ;  {:content (metamorphic-content)}
  ;
  ; @return (hiccup)
  [sidebar-id view-props]
  (let [content-props (components/extended-props->content-props view-props)]
       [:div.x-sidebar
         (engine/element-attributes sidebar-id view-props)
         [components/content sidebar-id content-props]]))

(defn view
  ; XXX#8711
  ; A sidebar elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A sidebar elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) sidebar-id
  ; @param (map) sidebar-props
  ;  {:class (string or vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :content-props (map)(opt)
  ;   :style (map)(opt)
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @usage
  ;  XXX#7610
  ;  A sidebar elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/sidebar {...}]
  ;
  ; @usage
  ;  [elements/sidebar :my-sidebar {...}]
  ;
  ; @return (component)
  ([sidebar-props]
   [view nil sidebar-props])

  ([sidebar-id sidebar-props]
   (let [sidebar-id    (a/id   sidebar-id)
         sidebar-props (a/prot sidebar-props sidebar-props-prototype)]
        [sidebar sidebar-id sidebar-props])))
