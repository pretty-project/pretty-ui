
(ns pretty-elements.button.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [dom.api :as dom]
              [pretty-build-kit.api :as pretty-build-kit]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  [{:keys [font-size on-click] :as button-props}]
  (cond-> button-props
          :effects/default        (pretty-build-kit/default-values      {:click-effect :opacity})
          :derive-fns/default     (pretty-build-kit/default-values      {})
          :text/default           (pretty-build-kit/default-values      {:font-size :s :font-weight :medium :line-height :text-block :text-overflow :hidden})
          :layout/default         (pretty-build-kit/default-values      {:horizontal-align :center})
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

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)
  ;  :on-mouse-over (function or Re-Frame metamorphic-event)
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
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)
  ;  :progress-color (keyword or string)
  ;  :progress-direction (keyword)
  ;  :progress-duration (ms)
  ;  :text-overflow (keyword)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [button-id {:keys [badge-content border-color font-size icon marker-color on-click on-mouse-over progress tooltip-content] :as button-props}]
  ; @note (#7861)
  ; Badge metamorphic ontent and tooltip metamorphic content must be composed to string content before they get passed to any Pretty CSS element attribute function.
  (merge {:click-effect     :opacity
          :focus-id         button-id
          :font-size        :s
          :font-weight      :medium
          :horizontal-align :center
          :line-height      :text-block
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
         (-> button-props)
         (if badge-content   {:badge-content   (metamorphic-content/compose badge-content)})
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})
         (if on-mouse-over   {:on-mouse-over   #(pretty-build-kit/dispatch-event-handler! on-mouse-over)})
         (if on-click        {:on-click        #(pretty-build-kit/dispatch-event-handler! on-click)
                              :on-mouse-up     #(dom/blur-active-element!)})))
