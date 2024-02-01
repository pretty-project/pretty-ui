
(ns components.copyright-label.views
    (:require [components.copyright-label.prototypes :as copyright-label.prototypes]
              [fruits.random.api                     :as random]
              [pretty-elements.api                   :as pretty-elements]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- copyright-label
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:font-size (keyword, px or string)}
  [_ {:keys [font-size]}]
  (let [server-year    @(r/subscribe [:x.core/get-server-year])]
        ;copyright-label (x.app-details/copyright-label server-year)]
       [:div.c-copyright-label {:data-color :muted
                                :data-text-selectable false}
        [:a.c-copyright-label--anchor {:data-font-weight :medium
                                       :data-font-size :xxs
                                       :data-letter-spacing :auto
                                       :data-line-height :text-block
                                       :style {:text-transform :uppercase}
                                       :href "https://monogo.app"
                                       :data-click-effect :opacity
                                       :data-orientation :horizontal}
                                      [:i.c-copyright-label--icon {:data-icon-family :material-symbols-outlined
                                                                   :data-icon-size   :xs}
                                                                  :copyright]]]))
        ;                              copyright-label]]))
       ;[pretty-elements/label ::copyright-label
        ;               {:color            :muted
        ;                :content          copyright-label
        ;                :font-size        font-size
        ;                :horizontal-align :center
        ;                :icon             :copyright
        ;                :indent           {:horizontal :xs}]))

(defn view
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:font-size (keyword, px or string)(opt)
  ;   Default: :xxs}
  ;
  ; @usage
  ; [copyright-label {...}]
  ;
  ; @usage
  ; [copyright-label :my-copyright-label {...}]
  ;
  ; @usage
  ; [copyright-label {...}]
  ([label-props]
   [view (random/generate-keyword) label-props])

  ([label-id label-props]
   ; @note (tutorials#parameterizing)
   (fn [_ label-props]
       (let [label-props (copyright-label.prototypes/label-props-prototype label-props)]
            [copyright-label label-id label-props]))))
