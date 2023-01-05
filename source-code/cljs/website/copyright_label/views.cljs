
(ns website.copyright-label.views
    (:require [elements.api                       :as elements]
              [random.api                         :as random]
              [re-frame.api                       :as r]
              [website.copyright-label.prototypes :as copyright-label.prototypes]
              [x.app-details                      :as x.app-details]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- copyright-label
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:color (keyword or string)}
  [_ {:keys [color]}]
  (let [server-year          @(r/subscribe [:x.core/get-server-year])
        copyright-information (x.app-details/copyright-information server-year)]
       [elements/label ::copyright-label
                       {:color            color
                        :content          copyright-information
                        :font-size        :xs
                        :horizontal-align :center
                        :outdent          {:bottom :xs :vertical :s}}]))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ; {:color (keyword or string)(opt)
  ;   Default: :inherit}
  ;
  ; @usage
  ; [copyright-label {...}]
  ;
  ; @usage
  ; [copyright-label :my-copyright-label {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   (let [component-props (copyright-label.prototypes/component-props-prototype component-props)]
        [copyright-label component-id component-props])))
