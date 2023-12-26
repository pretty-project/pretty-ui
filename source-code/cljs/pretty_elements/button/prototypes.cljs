
(ns pretty-elements.button.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [dom.api :as dom]
              [pretty-elements.element.side-effects :as element.side-effects]
              [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword)(opt)
  ;  :content (metamorphic-content)(opt)
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
  [{:keys [badge-content border-color content font-size icon marker-color on-click on-mouse-over progress tooltip-content] :as button-props}]
  ; @note (#7861)
  ; Badge content and tooltip content must be composed before they get passed to the Pretty CSS element attribute functions.
  (merge {:click-effect     :opacity
          :content-value-f  return
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
         (if on-mouse-over   {:on-mouse-over   #(element.side-effects/dispatch-event-handler! on-mouse-over)})
         (if on-click        {:on-click        #(element.side-effects/dispatch-event-handler! on-click)
                              :on-mouse-up     #(dom/blur-active-element!)})))
