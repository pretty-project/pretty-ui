
(ns pretty-elements.icon-button.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api :as pretty-build-kit]
              [dom.api :as dom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :badge-color (keyword or string)(opt
  ;  :marker-color (keyword or string)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :on-mouse-over (function or Re-Frame metamorphic-event)(opt)
  ;  :progress (percent)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword or string)
  ;  :badge-position (keyword)
  ;  :focus-id (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword, px or string)
  ;  :marker-position (keyword)
  ;  :progress-color (keyword or string)
  ;  :progress-direction (keyword)
  ;  :progress-duration (ms)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [button-id {:keys [badge-content border-color marker-color on-click on-mouse-over progress tooltip-content] :as button-props}]
  (merge {:focus-id    button-id
          :icon-family :material-symbols-outlined
          :icon-size   :m}
         (if badge-content   {:badge-color        :primary
                              :badge-position     :tr})
         (if border-color    {:border-position    :all
                              :border-width       :xxs})
         (if marker-color    {:marker-position    :tr})
         (if progress        {:progress-color     :muted
                              :progress-direction :ltr
                              :progress-duration  250})
         (if tooltip-content {:tooltip-position   :right})
         (-> button-props)
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})
         (if on-mouse-over   {:on-mouse-over   #(pretty-build-kit/dispatch-event-handler! on-mouse-over)})
         (if on-click        {:on-click        #(pretty-build-kit/dispatch-event-handler! on-click)
                              :on-mouse-up     #(dom/blur-active-element!)})))
