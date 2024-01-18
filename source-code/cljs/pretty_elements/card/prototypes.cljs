
(ns pretty-elements.card.prototypes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api    :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; @ignore
  ;
  ; @param (map) card-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :href (string)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword or string)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :cursor (keyword or string)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [{:keys [badge-content border-color href marker-color on-click] :as card-props}]
  ; @note (pretty-elements.button.prototypes#7861)
  ;
  ; @bug (#7901)
  ; Using the 'dom/blur-active-element!' function as 'on-mouse-up' event must be conditional.
  ; Otherwise, in case the card is not clickable and it contains a 'text-field' element
  ; the blur function would drop the focus of the field when the card gets clicked.
  (merge {}
         (if badge-content {:badge-color     :primary
                            :badge-position  :tr})
         (if border-color  {:border-position :all
                            :border-width    :xxs})
         (if marker-color  {:marker-position :tr})
         (if href          {:click-effect    :opacity})
         (if on-click      {:click-effect    :opacity})
         (-> card-props)
         (if badge-content {:badge-content (metamorphic-content/compose badge-content)})
         (if href          {:on-mouse-up   #(dom/blur-active-element!)})
         (if on-click      {:on-click      #(pretty-build-kit/dispatch-event-handler! on-click)
                            :on-mouse-up   #(dom/blur-active-element!)})))
