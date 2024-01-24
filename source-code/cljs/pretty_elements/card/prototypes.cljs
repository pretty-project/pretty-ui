
(ns pretty-elements.card.prototypes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-css.api :as pretty-css]))

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
  ;  :on-click-f (function)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword or string)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :cursor (keyword or string)
  ;  :on-click-f (function)
  ;  :on-mouse--fup (function)}
  [{:keys [badge-content border-color href marker-color on-click-f] :as card-props}]
  ; @note (pretty-elements.button.prototypes#7861)
  ;
  ; @bug (#7901)
  ; Using the 'dom/blur-active-element!' function as 'on-mouse-up-f' function must be conditional.
  ; Otherwise, in case the card is not clickable and it displays a 'text-field' input,
  ; the blur function would drop the focus of the field when the card gets clicked.
  (merge {}
         (if badge-content {:badge-color     :primary
                            :badge-position  :tr})
         (if border-color  {:border-position :all
                            :border-width    :xxs})
         (if marker-color  {:marker-position :tr})
         (if href          {:click-effect    :opacity})
         (if on-click-f    {:click-effect    :opacity})
         (-> card-props)
         (if badge-content {:badge-content (metamorphic-content/compose badge-content)})
         (if href          {:on-mouse-up-f dom/blur-active-element!})
         (if on-click-f    {:on-mouse-up   dom/blur-active-element!})))
