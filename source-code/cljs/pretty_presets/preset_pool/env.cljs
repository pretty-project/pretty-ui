
(ns pretty-presets.preset-pool.env
    (:require [map.api                          :as map]
              [pretty-presets.preset-pool.state :as preset-pool.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-preset
  ; @ignore
  ;
  ; @param (map) element-props
  ; {:preset (keyword)(opt)}
  ;
  ; @usage
  ; (apply-preset {:fill-color :muted
  ;                :preset     :my-preset})
  ;
  ; @example
  ; (reg-preset! :my-preset {:hover-color :highlight})
  ; (apply-preset {:fill-color :muted
  ;                :preset     :my-preset})
  ; =>
  ; {:fill-color  :muted
  ;  :hover-color :highlight
  ;  :preset      :my-preset}
  ;
  ; @return (map)
  [{:keys [preset] :as element-props}]
  ; 1. Takes the ':preset' property (if any) from the 'element-props' property map
  ; 2. Tries to look up a previously registered preset function / preset map in the preset pool atom,
  ;    under the key that was derived from the 'element-props' map (1. step).
  ; 3. Dissociates the ':preset' key from the 'element-props' map in order to avoid
  ;    infinite loops, because after the preset is applied this function runs itself
  ;    recursivelly to check whether the applied preset has associated another preset ID
  ;    into the 'element-props'.
  (if-let [preset (get @preset-pool.state/PRESETS preset)]
          (cond-> element-props :avoiding-infinite-loops (dissoc :preset)
                                (-> preset fn?)          (preset)
                                (-> preset map?)         (map/reversed-merge preset)
                                :recursivelly-applying   (apply-preset))
          (-> element-props)))
