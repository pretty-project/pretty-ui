
(ns pretty-elements.button.prototypes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-defaults.api :as pretty-defaults]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  [{:keys [font-size on-click-f] :as button-props}]
  (cond-> button-props
          :effects/default        (pretty-defaults/use-default-values      {:click-effect :opacity})
          :derive-fns/default     (pretty-defaults/use-default-values      {})
          :text/default           (pretty-defaults/use-default-values      {:font-size :s :font-weight :medium :line-height :text-block :text-overflow :hidden})
          :layout/default         (pretty-defaults/use-default-values      {:horizontal-align :center})
          :badge/default          (pretty-defaults/use-default-value-group {:badge-content nil :badge-color :primary :badge-position :tr})
          :border/default         (pretty-defaults/use-default-value-group {:border-color :primary :border-position :all :border-width :xxs})
          :icon/default           (pretty-defaults/use-default-value-group {:icon nil :icon-color :primary :icon-position :left :icon-size (or font-size :s)})
          :progress/default       (pretty-defaults/use-default-value-group {:progress nil :progress-color :muted :progress-direction :ltr :progress-duration 250})
          :tooltip/default        (pretty-defaults/use-default-value-group {:tooltip-content nil :tooltip-position :right})
          :badge-content/update   (pretty-defaults/value-update-fns    {:badge-content   metamorphic-content/compose})
          :tooltip-content/update (pretty-defaults/value-update-fns    {:tooltip-content metamorphic-content/compose})
          (-> on-click-f)         (pretty-defaults/force-values       {:on-mouse-up-f   dom/blur-active-element!})))

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :progress (percent)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword or string)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :focus-id (keyword)
  ;  :font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :horizontal-align (keyword)
  ;  :icon-family (keyword)
  ;  :icon-position (keyword)
  ;  :icon-size (keyword, px or string)
  ;  :line-height (keyword, px or string)
  ;  :on-mouse-up-f (function)
  ;  :progress-color (keyword or string)
  ;  :progress-direction (keyword)
  ;  :progress-duration (ms)
  ;  :text-overflow (keyword)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [button-id {:keys [badge-content border-color font-size href-uri icon marker-color on-click-f progress tooltip-content] :as button-props}]
  ; @note (#7861)
  ; Badge metamorphic ontent and tooltip metamorphic content must be composed to string content before they get passed to any Pretty CSS element attribute function.
  (merge {:focus-id         button-id
          :font-size        :s
          :font-weight      :medium
          :horizontal-align :center
          :line-height      :text-block
          :letter-spacing   :auto
          :text-overflow    :hidden}
         (if badge-content   {:badge-color        :primary
                              :badge-position     :tr})
         (if border-color    {:border-position    :all
                              :border-width       :xxs})
         (if marker-color    {:marker-position    :tr})
         (if icon            {:icon-family        :material-symbols-outlined
                              :icon-position      :left
                              :icon-size          (or font-size :s)})
         (if progress        {:progress-color     :muted
                              :progress-direction :ltr
                              :progress-duration  250})
         (if tooltip-content {:tooltip-position   :right})
         (if href-uri        {:click-effect       :opacity})
         (if on-click-f      {:click-effect       :opacity})
         (-> button-props)
         ;{:label (metamorphic-content/compose label placeholder)}
         (if badge-content   {:badge-content   (metamorphic-content/compose badge-content)})
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})

         (if href-uri        {:on-mouse-up-f dom/blur-active-element!})
         (if on-click-f      {:on-mouse-up-f dom/blur-active-element!})))
