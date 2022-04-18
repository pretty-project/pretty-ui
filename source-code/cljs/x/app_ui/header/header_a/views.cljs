
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.views
    (:require [reagent.api             :as reagent]
              [x.app-core.api          :as a]
              [x.app-ui.header.helpers :as header.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-sensor
  ; @param (keyword)(opt) sensor-id
  ; @param (map) sensor-props
  ;  {:offset (px)(opt)
  ;   :title (metamorphic-content)}
  ([sensor-props]
   [title-sensor (a/id) sensor-props])

  ([sensor-id sensor-props]
   (reagent/lifecycles {:component-did-mount    (fn [] (header.helpers/sensor-did-mount-f    sensor-id sensor-props))
                        :component-will-unmount (fn [] (header.helpers/sensor-will-unmount-f sensor-id))
                        :component-did-update   (fn [this] (let [[_ sensor-props] (reagent/arguments this)]
                                                                (header.helpers/sensor-did-update-f sensor-id sensor-props)))})))
