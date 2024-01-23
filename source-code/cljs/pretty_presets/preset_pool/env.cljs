
(ns pretty-presets.preset-pool.env
    (:require [fruits.map.api                   :as map]
              [pretty-presets.preset-pool.state :as preset-pool.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-preset
  ; @ignore
  ;
  ; @param (keyword)(opt) item-id
  ; @param (map) item-props
  ; {:preset (keyword)(opt)}
  ;
  ; @usage
  ; (apply-preset {:fill-color :muted
  ;                :preset     :my-preset})
  ;
  ; @usage
  ; (reg-preset! :my-preset {:hover-color :highlight})
  ; (apply-preset {:fill-color :muted
  ;                :preset     :my-preset})
  ; =>
  ; {:fill-color  :muted
  ;  :hover-color :highlight
  ;  :preset      :my-preset}
  ;
  ; @return (map)
  ([item-props]
   (apply-preset nil item-props))
   
  ([_ {:keys [preset] :as item-props}]
   ; 1. Takes the ':preset' property (if any) from the 'item-props' property map
   ; 2. Looks up a preset function / preset map in the preset pool atom,
   ;    registered with the key that was derived from the 'item-props' map (1. step).
   ; 3. Dissociates the ':preset' key from the 'item-props' map in order to avoid
   ;    infinite loops, because after the preset is applied this function runs itself
   ;    recursivelly to check whether the applied preset has associated another preset ID to the 'item-props'.
   (if-let [preset (get @preset-pool.state/PRESETS preset)]
           (cond-> item-props :avoiding-infinite-loops (dissoc :preset)
                              (-> preset fn?)          (preset)
                              (-> preset map?)         (map/reversed-merge preset)
                              :recursive-applying      (apply-preset))
           (-> item-props))))
