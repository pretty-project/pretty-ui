
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.28
; Description:
; Version: v0.2.6
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-stickers
    (:require [mid-fruits.candy      :refer [param]]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sticker-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sticker-props
  ;
  ; @return (map)
  ;  {:icon-family (keyword)}
  [sticker-props]
  (merge {:icon-family :material-icons-filled}
         (param sticker-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-sticker-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map) sticker-props
  ;  {:icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :on-click (metamorphic-event)
  ;   :tooltip (metamorphic-content)(opt)}
  [element-id _ {:keys [icon icon-family on-click tooltip]}]
  [:button.x-element--sticker-button {:on-click        #(a/dispatch on-click)
                                      :on-mouse-up     #(environment/blur-element!)
                                      :title            (components/content {:content tooltip})
                                      :data-icon-family (param icon-family)}
                                     (param icon)])

(defn- element-sticker-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map) sticker-props
  ;  {:disabled? (boolean)(opt)
  ;   :icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled}
  [_ _ {:keys [disabled? icon icon-family]}]
  [:i.x-element--sticker-icon (if disabled? {:data-disabled true
                                             :data-icon-family (param icon-family)}
                                            {:data-icon-family (param icon-family)})
                              (param icon)])

(defn- element-sticker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map) sticker-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  [element-id element-props {:keys [disabled? on-click] :as sticker-props}]
  (if (and (some? on-click)
           (not   disabled?))
      [element-sticker-button element-id element-props sticker-props]
      [element-sticker-icon   element-id element-props sticker-props]))

(defn element-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:stickers (maps in vector)(opt)}
  [element-id {:keys [stickers] :as element-props}]
  (if (vector/nonempty? stickers)
      (reduce (fn [%1 %2] (let [%2 (sticker-props-prototype %2)]
                               (conj %1 [element-sticker element-id element-props %2])))
              [:div.x-element--stickers] stickers)))
