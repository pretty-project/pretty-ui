
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.25
; Description:
; Version: v0.4.2
; Compatibility: x4.4.2



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
  ;   :layout (keyword)}
  [anchor-props]
  (merge {:color  :primary
          :layout :row}
         (param anchor-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- anchor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;
  ; @return (hiccup)
  [anchor-id anchor-props]
  (let [content-props (components/extended-props->content-props anchor-props)]
       [:a.x-anchor (engine/element-attributes anchor-id anchor-props
                      (engine/clickable-body-attributes anchor-id anchor-props))
                    [components/content content-props]]))

(defn view
  ; XXX#9085
  ; Az anchor elem {:on-click [:router/go-to! "..."]} paraméterezés helyett
  ; {:href "..."} paraméterezéssel való használata lehetővé teszi az útvonal új lapon
  ; történő megnyitását.
  ;
  ; @param (keyword)(opt) anchor-id
  ; @param (map) anchor-props
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :class (string or vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :href (string)(opt)
  ;     Only w/o {:on-click ...}
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
   [view nil anchor-props])

  ([anchor-id anchor-props]
   (let [anchor-id    (a/id   anchor-id)
         anchor-props (a/prot anchor-props anchor-props-prototype)]
        [anchor anchor-id anchor-props])))
