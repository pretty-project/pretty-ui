
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.27
; Description:
; Version: v0.8.8
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.polarity
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- polarity-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ;  {:orientation (keyword)}
  [polarity-props]
  (merge {:orientation :horizontal}
         (param polarity-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- start-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:orientation (keyword)
  ;   :start-content (metamorphic-content)}
  ;
  ; @return (component)
  [polarity-id {:keys [orientation start-content]}]
  (if (some? start-content)
      [:div.x-polarity--start-content [components/content {:content start-content}]]
      [:div.x-polarity--placeholder]))

(defn- middle-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:middle-content (metamorphic-content)
  ;   :orientation (keyword)}
  ;
  ; @return (component)
  [polarity-id {:keys [middle-content orientation]}]
  (if (some? middle-content)
      [:div.x-polarity--middle-content [components/content {:content middle-content}]]
      [:div.x-polarity--placeholder]))

(defn- end-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:end-content (metamorphic-content)
  ;   :orientation (keyword)}
  ;
  ; @return (component)
  [polarity-id {:keys [end-content orientation]}]
  (if (some? end-content)
      [:div.x-polarity--end-content [components/content {:content end-content}]]
      [:div.x-polarity--placeholder]))

(defn- polarity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;
  ; @return (hiccup)
  [polarity-id polarity-props]
  [:div.x-polarity (engine/element-attributes polarity-id polarity-props)
                   [start-content  polarity-id polarity-props]
                   [middle-content polarity-id polarity-props]
                   [end-content    polarity-id polarity-props]])

(defn view
  ; @param (keyword)(opt) polarity-id
  ; @param (map) polarity-props
  ;  {:class (string or vector)(opt)
  ;   :orientation (keyword)(opt)
  ;    :horizontal, vertical
  ;    Default: :horizontal
  ;   :end-content (metamorphic-content)
  ;   :middle-content (metamorphic-content)
  ;   :style (map)(opt)
  ;   :start-content (metamorphic-content)(opt)
  ;
  ; @usage
  ;  [elements/polarity {...}]
  ;
  ; @usage
  ;  [elements/polarity :my-polarity {...}]
  ;
  ; @usage
  ;  [elements/polarity {:start-content [:<> [elements/label {:content "My label"}]
  ;                                          [elements/label {:content "My label"}]]}]
  ;
  ; @return (component)
  ([polarity-props]
   [view (a/id) polarity-props])

  ([polarity-id polarity-props]
   (let [polarity-props (a/prot polarity-props polarity-props-prototype)]
        [polarity polarity-id polarity-props])))
