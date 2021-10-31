
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.28
; Description:
; Version: v0.2.2
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-stickers
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.engine.focusable :as focusable]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-sticker-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ; @param (map) sticker-props
  ;  {:icon (keyword) Material icon class
  ;   :on-click (metamorphic-event)
  ;   :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [element-id _ {:keys [icon on-click tooltip]}]
  [:button.x-element--sticker-button
    {:on-click   #(a/dispatch on-click)
     :on-mouse-up (focusable/blur-element-function element-id)
     :title       (components/content {:content tooltip})}
    (keyword/to-dom-value icon)])

(defn- element-sticker-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ; @param (map) sticker-props
  ;  {:disabled? (boolean)(opt)
  ;   :icon (keyword) Material icon class}
  ;
  ; @return (hiccup)
  [_ _ {:keys [disabled? icon]}]
  [:i.x-element--sticker-icon
    (if (boolean disabled?)
        {:data-disabled true})
    (keyword/to-dom-value icon)])

(defn- element-sticker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ; @param (map) sticker-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [element-id view-props {:keys [disabled? on-click] :as sticker-props}]
  (if (and (some? on-click)
           (not   disabled?))
      [element-sticker-button element-id view-props sticker-props]
      [element-sticker-icon   element-id view-props sticker-props]))

(defn element-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ;  {:stickers (maps in vector)(opt)}
  ;
  ; @return (hiccup)
  [element-id {:keys [stickers] :as view-props}]
  (if (vector/nonempty? stickers)
      (reduce #(vector/conj-item %1 [element-sticker element-id view-props %2])
               [:div.x-element--stickers] stickers)))
