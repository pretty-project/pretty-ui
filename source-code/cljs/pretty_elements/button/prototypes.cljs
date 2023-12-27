
(ns pretty-elements.button.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [dom.api :as dom]
              [pretty-elements.element.side-effects :as element.side-effects]
              [fruits.noop.api :refer [return]]
              [pretty-build-kit.api :as pretty-build-kit]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  [{:keys [font-size on-click] :as button-props}]
  (cond-> button-props
          :effects/default        (pretty-build-kit/default-values      {:click-effect :opacity})
          :derive-fns/default     (pretty-build-kit/default-values      {:content-value-f return :placeholder-value-f return})
          :text/default           (pretty-build-kit/default-values      {:font-size :s :font-weight :medium :line-height :text-block :text-overflow :hidden})
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

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword)(opt)
  ;  :font-size (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)
  ;  :on-mouse-over (function or Re-Frame metamorphic-event)
  ;  :progress (percent)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :click-effect (keyword)
  ;  :content-value-f (function)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon-family (keyword)
  ;  :icon-position (keyword)
  ;  :icon-size (keyword)
  ;  :line-height (keyword)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)
  ;  :progress-color (keyword)
  ;  :progress-direction (keyword)
  ;  :progress-duration (ms)
  ;  :text-overflow (keyword)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [{:keys [badge-content border-color font-size icon marker-color on-click on-mouse-over progress tooltip-content] :as button-props}]
  ; @note (#7861)
  ; Badge content and tooltip content must be composed before they get passed to the Pretty CSS element attribute functions.
  (merge {:click-effect     :opacity
          :font-size        :s
          :font-weight      :medium
          :horizontal-align :center
          :line-height      :text-block
          :text-overflow    :hidden
          :content-value-f return
          :placeholder-value-f return}
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
         (if on-mouse-over   {:on-mouse-over   #(element.side-effects/dispatch-event-handler! on-mouse-over)})
         (if on-click        {:on-click        #(element.side-effects/dispatch-event-handler! on-click)
                              :on-mouse-up     #(dom/blur-active-element!)})))
