
(ns pretty-elements.chip.prototypes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api    :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  [{:keys [font-size on-click] :as button-props}]
  ; nem biztos hogy mind kell a chip-hez!!!!
  (cond-> button-props
          :derive-fns/default     (pretty-build-kit/default-values      {})
          :text/default           (pretty-build-kit/default-values      {:text-color :default})
          :layout/default         (pretty-build-kit/default-values      {})
          :badge/default          (pretty-build-kit/default-value-group {:badge-content nil :badge-color :primary :badge-position :tr})
          :border/default         (pretty-build-kit/default-value-group {:border-color :primary :border-position :all :border-width :xxs})
          :icon/default           (pretty-build-kit/default-value-group {:icon nil :icon-color :primary :icon-position :left :icon-size (or font-size :s)})
          :progress/default       (pretty-build-kit/default-value-group {:progress nil :progress-color :muted :progress-direction :ltr :progress-duration  250})
          :tooltip/default        (pretty-build-kit/default-value-group {:tooltip-content nil :tooltip-position :right})
          :badge-content/update   (pretty-build-kit/value-update-fns    {:badge-content   metamorphic-content/compose})
          :tooltip-content/update (pretty-build-kit/value-update-fns    {:tooltip-content metamorphic-content/compose})
          :on-mouse-over/wrap     (pretty-build-kit/value-wrap-fns      {:on-mouse-over   pretty-build-kit/dispatch-event-handler!})
          :on-click/wrap          (pretty-build-kit/value-wrap-fns      {:on-click        pretty-build-kit/dispatch-event-handler!})
          (-> on-click)           (pretty-build-kit/forced-values       {:on-mouse-up     dom/blur-active-element!})))

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (map) chip-props
  ; {:href-uri (string)
  ;  :on-click-f (function)(opt)}
  ;
  ; @return (map)
  ; {:text-color (keyword or string)}
  [{:keys [href-uri on-click-f] :as chip-props}]
  (merge {:text-color :default}
         (if href-uri   {:click-effect :opacity})
         (if on-click-f {:click-effect :opacity})
         (-> chip-props)
         (if href-uri   {:on-mouse-up-f dom/blur-active-element!})
         (if on-click-f {:on-mouse-up-f dom/blur-active-element!})))
