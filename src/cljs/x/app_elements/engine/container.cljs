
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v1.0.8
; Compatibility: x4.3.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.container
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.engine.constant-props :as constant-props]
              [x.app-elements.engine.element        :as element]
              [x.app-elements.engine.focusable      :as focusable]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-props->render-element-overlay?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (boolean)
  [{:keys [disabled?]}]
  (boolean disabled?))

(defn context-props->stated-container?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;
  ; @return (boolean)
  [context-props]
  (components/extended-props->stated-props? context-props))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-container-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ;  {:container-floating? (boolean)(opt)
  ;   :container-position (keyword)(opt)
  ;   :container-stretch-orientation (keyword)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :highlighted? (boolean)(opt)
  ;   :status-animation? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:data-animation (string)
  ;  :data-disabled (boolean)
  ;  :data-floating (boolean)
  ;  :data-highlighted (boolean)
  ;  :data-position (string)
  ;  :data-stretch-orientation (boolean)}
  [_ {:keys [container-floating? container-position container-stretch-orientation
             disabled? highlighted? status-animation?]}]
  (cond-> (param {})
          (some? container-position)
          (assoc :data-position (keyword/to-dom-value container-position))
          (some? container-stretch-orientation)
          (assoc :data-stretch-orientation (keyword/to-dom-value container-stretch-orientation))
          (boolean container-floating?) (assoc :data-floating    true)
          (boolean disabled?)           (assoc :data-disabled    true)
          (boolean highlighted?)        (assoc :data-highlighted true)
          (boolean status-animation?)   (assoc :data-animation (keyword/to-dom-value :status))))

(defn container-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ;  {:info-tooltip (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [info-tooltip]}]
  (cond-> (param {})
          (some? info-tooltip)
          ; XXX#5491 Element info-tooltip padding
          (assoc :style {:paddingRight "36px"})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-context-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ;  {:context-surface (map)(opt)
  ;   {:content (metamorphic-content)
  ;    :content-props (map)(opt)
  ;    :subscriber (subscription vector)(opt)}
  ;   :render-context-surface? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [element-id {:keys [context-surface render-context-surface?]}]
  (if (boolean render-context-surface?)
      [:div.x-element-container--context-surface
        {:on-context-menu #(.preventDefault %)}
        [components/content element-id context-surface]]))

(defn- element-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ;  {:helper (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ {:keys [helper]}]
  (if (some? helper)
      [:div.x-element-container--helper [components/content {:content helper}]]))

(defn- element-info-tooltip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ;  {:info-tooltip (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ {:keys [info-tooltip]}]
  (if (some? info-tooltip)
      [:div.x-element-container--info-tooltip
         [:i.x-element-container--info-tooltip--icon
            (keyword/to-dom-value :info_outline)]
         [:div.x-element-container--info-tooltip--body
            [:div.x-element-container--info-tooltip--content
               [components/content {:content info-tooltip}]]]]))

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
  [:button.x-element-container--stickers--sticker-button
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
  [:i.x-element-container--stickers--sticker-icon
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

(defn- element-stickers
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
               [:div.x-element-container--stickers] stickers)))

(defn- element-container-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) context-props
  ;  {:component (component)}
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [element-id {:keys [component]} view-props]
  [:div.x-element-container--body
    (container-body-attributes element-id view-props)
    [element-info-tooltip      element-id view-props]
    [:div.x-element-container--element
      [component element-id view-props]]
    [element-stickers        element-id view-props]
    [element-context-surface element-id view-props]
    (if (view-props->render-element-overlay? view-props)
        [:div.x-element-container--overlay])])

(defn- element-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) context-props
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [element-id context-props view-props]
  [:div.x-element-container
    (element-container-attributes element-id view-props)
    [element-container-body       element-id context-props view-props]
    [element-helper               element-id view-props]])

(defn- stated-engine
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) context-props
  ;  {:base-props (map)
  ;    {:disabler (subscription vector)(opt)}
  ;   :initializer (metamorphic-event)(opt)
  ;   :modifier (function)(opt)
  ;   :subscriber (subscription vector)(opt)
  ;   :updater (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [element-id {:keys [base-props destructor initial-props initializer modifier
                      subscriber updater] :as context-props}]
  [components/stated element-id
    {:base-props         base-props
     :component          #'element-container
     :destructor         destructor
     :disabler           (get base-props :disabler)
     :initial-props      (constant-props/base-props->initial-props base-props)
     :initial-props-path (element/element-props-path element-id)
     :initializer        initializer
     :modifier           modifier
     :static-props       context-props
     :subscriber         subscriber
     :updater            updater}])

(defn- static-engine
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) context-props
  ;  {:base-props (map)}
  ;
  ; @return (component)
  [element-id {:keys [base-props] :as context-props}]
  [element-container element-id context-props base-props])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) context-props
  ;  {:base-props (map)
  ;    {:container-floating? (boolean)(opt)
  ;      Default: false
  ;     :container-position (keyword)(opt)
  ;      :tl, :tr, :br, :bl
  ;      Only w/ {:container-floating? true}
  ;     :container-stretch-orientation (keyword)(opt)
  ;      :horizontal, :vertical, :both, :none
  ;      Default: :none
  ;     :disabler (subscription vector)(opt)
  ;     :helper (metamorphic-content)(opt)
  ;     :info-tooltip (metamorphic-content)(opt)
  ;     :stickers (maps in vector)(opt)
  ;      [{:disabled? (boolean)(opt)
  ;         Default: false
  ;        :icon (keyword) Material icon class
  ;        :on-click (metamorphic-event)(opt)
  ;        :tooltip (metamorphic-content)(opt)}]
  ;     :tooltip (metamorphic-content)(opt)}
  ;   :component (component)
  ;   :destructor (metamorphic-event)(opt)
  ;   :initializer (metamorphic-event)(opt)
  ;   :subscriber (subscription vector)(opt)
  ;   :updater (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [element-id context-props]
  (if (context-props->stated-container? context-props)
      [stated-engine element-id context-props]
      [static-engine element-id context-props]))
