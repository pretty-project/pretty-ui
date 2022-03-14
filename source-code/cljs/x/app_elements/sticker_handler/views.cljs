
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.sticker-handler.views
    (:require [mid-fruits.candy                          :refer [param]]
              [mid-fruits.vector                         :as vector]
              [x.app-components.api                      :as components]
              [x.app-core.api                            :as a]
              [x.app-elements.sticker-handler.prototypes :as sticker-handler.prototypes]
              [x.app-environment.api                     :as environment]))



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
                                      :title            (components/content tooltip)
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
                                             :data-icon-family icon-family}
                                            {:data-icon-family icon-family})
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
      (letfn [(f [stickers sticker-props]
                 (let [sticker-props (sticker-handler.prototypes/sticker-props-prototype sticker-props)]
                      (conj stickers [element-sticker element-id element-props sticker-props])))]
             (reduce f [:div.x-element--stickers] stickers))))
