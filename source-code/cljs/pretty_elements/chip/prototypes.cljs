
(ns pretty-elements.chip.prototypes
    (:require [fruits.noop.api :refer [return]]
              [pretty-build-kit.api :as pretty-build-kit]
              [dom.api :as dom]
              [pretty-elements.element.side-effects :as element.side-effects]
              [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  [{:keys [font-size on-click] :as button-props}]
  (cond-> button-props
          :derive-fns/default     (pretty-build-kit/default-values      {:content-value-f return :placeholder-value-f return})
          :text/default           (pretty-build-kit/default-values      {:text-color :default})
          :layout/default         (pretty-build-kit/default-values      {:horizontal-align :center})
          :badge/default          (pretty-build-kit/default-value-group {:badge-content nil :badge-color :primary :badge-position :tr})
          :border/default         (pretty-build-kit/default-value-group {:border-color :primary :border-position :all :border-width :xxs})
          :icon/default           (pretty-build-kit/default-value-group {:icon nil :icon-color :primary :icon-position :left :icon-size (or font-size :s)})
          :progress/default       (pretty-build-kit/default-value-group {:progress nil :progress-color :muted :progress-direction :ltr :progress-duration  250})
          :tooltip/default        (pretty-build-kit/default-value-group {:tooltip-content nil :tooltip-position :right})
          :badge-content/update   (pretty-build-kit/value-update-fns    {:badge-content   metamorphic-content/compose})
          :tooltip-content/update (pretty-build-kit/value-update-fns    {:tooltip-content metamorphic-content/compose})
          :on-mouse-over/wrap     (pretty-build-kit/value-wrap-fns      {:on-mouse-over   element.side-effects/dispatch-event-handler!})
          :on-click/wrap          (pretty-build-kit/value-wrap-fns      {:on-click        element.side-effects/dispatch-event-handler!})
          (-> on-click)           (pretty-build-kit/forced-values       {:on-mouse-up     dom/blur-active-element!})))

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (map) chip-props
  ; {:icon (keyword)(opt)
  ;  :primary-button (map)(opt)}
  ;
  ; @return (map)
  ; {:border-radius (map)
  ;  :content-value-f (function)
  ;  :icon-family (keyword)
  ;  :primary-button (map)
  ;   {:icon-family (keyword)}
  ;  :text-color (keyword or string)}
  [{:keys [icon primary-button] :as chip-props}]
  (merge {:content-value-f  return
          :placeholder-value-f return
          :text-color       :default}
         (if icon           {:icon-family :material-symbols-outlined})
         (-> chip-props)
         (if primary-button {:primary-button (merge {:icon-family :material-symbols-outlined} primary-button)})))
