
(ns pretty-elements.chip.prototypes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-defaults.api     :as pretty-defaults]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  [{:keys [font-size on-click] :as button-props}]
  ; nem biztos hogy mind kell a chip-hez!!!!
  (cond-> button-props
          :derive-fns/default     (pretty-defaults/use-default-values      {})
          :text/default           (pretty-defaults/use-default-values      {:text-color :default})
          :layout/default         (pretty-defaults/use-default-values      {})
          :badge/default          (pretty-defaults/use-default-value-group {:badge-content nil :badge-color :primary :badge-position :tr})
          :border/default         (pretty-defaults/use-default-value-group {:border-color :primary :border-position :all :border-width :xxs})
          :icon/default           (pretty-defaults/use-default-value-group {:icon nil :icon-color :primary :icon-position :left :icon-size (or font-size :s)})
          :progress/default       (pretty-defaults/use-default-value-group {:progress nil :progress-color :muted :progress-direction :ltr :progress-duration  250})
          :tooltip/default        (pretty-defaults/use-default-value-group {:tooltip-content nil :tooltip-position :right})
          ;:badge-content/update   (pretty-defaults/value-update-fns    {:badge-content   metamorphic-content/compose})
          ;:tooltip-content/update (pretty-defaults/value-update-fns    {:tooltip-content metamorphic-content/compose})
          (-> on-click)           (pretty-defaults/force-values       {:on-mouse-up     dom/blur-active-element!})))

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
