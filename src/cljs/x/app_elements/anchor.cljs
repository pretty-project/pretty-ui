
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.25
; Description:
; Version: v0.2.4
; Compatibility: x3.9.9



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
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [anchor-id view-props]
  (let [content-props (components/extended-props->content-props view-props)]
       [:a.x-anchor (engine/element-attributes anchor-id view-props
                      (engine/clickable-body-attributes anchor-id view-props))
                    [components/content content-props]]))

(defn view
  ; XXX#8711
  ; Az anchor elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; Az anchor elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; XXX#9085
  ; Az anchor elem {:on-click [:x.app-router/go-to! "..."]} paraméterezés helyett
  ; {:href "..."} paraméterezéssel való használata lehetővé teszi az útvonal új lapon
  ; történő megnyitását.
  ;
  ; @param (keyword)(opt) anchor-id
  ; @param (map) anchor-props
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :class (string or vector)(opt)
  ;   :content (metamorphic-content)
  ;   :content-props (map)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :href (string)(opt)
  ;     Only w/o {:on-click ...}
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-click (metamorphic-event)(constant)
  ;     Only w/o {:href ...}
  ;   :request-id (keyword)(constant)(opt)
  ;   :status-animation? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:request-id ...}
  ;   :style (map)(opt)
  ;   :subscriber (subscription vector)(opt)}
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
        [engine/container anchor-id
         {:base-props anchor-props
          :component  anchor
          :subscriber [:x.app-elements.button/get-view-props anchor-id]}])))
