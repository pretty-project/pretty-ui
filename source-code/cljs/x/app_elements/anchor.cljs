
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.25
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.anchor
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- anchor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) anchor-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :layout (keyword)}
  [anchor-props]
  (merge {:color     :primary
          :font-size :s
          :layout    :row}
         (param anchor-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- anchor-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;
  ; @return (hiccup)
  [anchor-id anchor-props]
  (let [content-props (components/extended-props->content-props anchor-props)]
       [:a.x-anchor--body (engine/clickable-body-attributes anchor-id anchor-props)
                          [components/content content-props]]))

(defn- anchor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;
  ; @return (hiccup)
  [anchor-id anchor-props]
  ; Az anchor elemet azért szükséges felosztani anchor és anchor-body komponensekre,
  ; hogy a disabled állapotot megfelelően lehessen alkalmazni.
  ; Az elemet disabled állapotában eltakaró overlay az elem kattintható komponensének
  ; vagy a kattintható komponens valamely ősének szomszédos eleme kell legyen.
  [:div.x-anchor (engine/element-attributes anchor-id anchor-props)
                 [anchor-body anchor-id anchor-props]])

(defn element
  ; XXX#9085
  ; Az anchor elem {:on-click [:router/go-to! "..."]} paraméterezés helyett
  ; {:href "..."} paraméterezéssel való használata lehetővé teszi az útvonal új lapon
  ; történő megnyitását.
  ;
  ; @param (keyword)(opt) anchor-id
  ; @param (map) anchor-props
  ;  {:color (keyword)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :href (string)(opt)
  ;     Only w/o {:on-click ...}
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-click (metamorphic-event)(constant)
  ;     Only w/o {:href ...}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/anchor {...}]
  ;
  ; @usage
  ;  [elements/anchor :my-anchor {...}]
  ;
  ; @return (component)
  ([anchor-props]
   [element (a/id) anchor-props])

  ([anchor-id anchor-props]
   (let [anchor-props (anchor-props-prototype anchor-props)]
        [anchor anchor-id anchor-props])))
