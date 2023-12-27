
(ns pretty-elements.card.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [fruits.noop.api :refer [return]]
              [dom.api :as dom]
              [pretty-elements.element.side-effects :as element.side-effects]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; @ignore
  ;
  ; @param (map) card-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword)(opt)
  ;  :href (string)(opt)
  ;  :marker-color (keyword)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :click-effect (keyword)
  ;  :content-value-f (function)
  ;  :cursor (keyword)
  ;  :marker-color (keyword)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [{:keys [badge-content border-color href marker-color on-click] :as card-props}]
  ; @note (pretty-elements.button.prototypes#7861)
  ;
  ; @bug (#7901)
  ; Using the 'dom/blur-active-element!' function as 'on-mouse-up' event must be conditional.
  ; Otherwise, in case the card is not clickable and it contains a 'text-field' element
  ; the blur function would drop the focus of the field when the card gets clicked.
  (merge {:content-value-f return
          :placeholder-value-f return}
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
         (if on-click      {:on-click      #(element.side-effects/dispatch-event-handler! on-click)
                            :on-mouse-up   #(dom/blur-active-element!)})))
