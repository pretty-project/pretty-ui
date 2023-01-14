
(ns components.copyright-label.views
    (:require [components.copyright-label.prototypes :as copyright-label.prototypes]
              [elements.api                          :as elements]
              [random.api                            :as random]
              [re-frame.api                          :as r]
              [x.app-details                         :as x.app-details]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- copyright-label
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:font-size (keyword)}
  [_ {:keys [font-size]}]
  (let [server-year    @(r/subscribe [:x.core/get-server-year])
        copyright-label (x.app-details/copyright-label server-year)]
       [:div.c-copyright-label {:data-color :muted
                                :data-selectable false}
        [:a.c-copyright-label--anchor {:data-font-weight :medium
                                       :data-font-size :xxs
                                       :data-line-height :text-block
                                       :style {:text-transform :uppercase}
                                       :href "https://monogo.app"
                                       :data-click-effect :opacity
                                       :data-orientation :horizontal}
                                      [:i.c-copyright-label--icon {:data-icon-family :material-icons-filled
                                                                   :data-icon-size :xs}
                                                                  :copyright]
                                      copyright-label]]))
       ;[elements/label ::copyright-label
        ;               {:color            :muted
        ;                :content          copyright-label
        ;                :font-size        font-size
        ;                :horizontal-align :center
        ;                :icon             :copyright
        ;                :indent           {:horizontal :xs}]))

(defn component
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:font-size (keyword)(opt)
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
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (let [label-props (copyright-label.prototypes/label-props-prototype label-props)]
        [copyright-label label-id label-props])))