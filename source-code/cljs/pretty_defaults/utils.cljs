
(ns pretty-defaults.utils
    (:require [fruits.map.api    :as map]
              [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-default-values
  ; @description
  ; - Replaces NIL or missing values in the given 'element-props' map with corresponding values from the given 'default-props' map(s).
  ; - Updates map type values in the given 'element-props' map by merging them with corresponding values from the given 'default-props' map(s).
  ;
  ; @param (map) element-props
  ; @param (list of maps) default-props
  ;
  ; @usage
  ; (use-default-values {:text-color :muted ...}
  ;                     {:text-color :highlight
  ;                      :width      :s})
  ; =>
  ; {:text-color :muted
  ;  :width      :s
  ;  ...}
  ;
  ; @usage
  ; (use-default-values {:surface {:border-color :primary} ...}
  ;                     {:surface {:fill-color   :highlight}})
  ; =>
  ; {:surface {:border-color :primary
  ;            :fill-color   :highlight}
  ;  ...}
  ;
  ; @usage
  ; Multiple 'default-props' maps can be provided:
  ; (use-default-values {...} ; <- The 'element-props' map is the PRIMARY source of values.
  ;                     {...} ; <- The list of 'default-props' maps is the SECONDARY source of values.
  ;                     {...} ...)
  ;
  ; @return (map)
  [element-props & default-props]
  (let [default-props (apply map/deep-merge default-props)]
       (map/deep-merge default-props element-props)))

(defn use-default-value-group
  ; @description
  ; - Replaces NIL or missing values in the given 'element-props' map with corresponding values from the given 'default-props' map(s)
  ;   in case at least one key of the given 'default-props' map(s) is present in the given 'element-props' map.
  ; - Updates map type values in the given 'element-props' map by merging them with corresponding values from the given 'default-props' map(s)
  ;   in case at least one key of the given 'default-props' map(s) is present in the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (list of maps) default-props
  ;
  ; @usage
  ; (use-default-value-group {:icon        :people ...}
  ;                          {:icon        :close
  ;                           :icon-family :material-icons-outlined
  ;                           :icon-size   :s})
  ; =>
  ; {:icon        :people
  ;  :icon-family :material-icons-outlined
  ;  :icon-size   :s
  ;  ...}
  ;
  ; @usage
  ; Multiple 'default-props' maps can be provided:
  ; (use-default-value-group {...} ; <- The 'element-props' map is the PRIMARY source of values.
  ;                          {...} ; <- The list of 'default-props' maps is the SECONDARY source of values.
  ;                          {...} ...)
  ;
  ; @return (map)
  [element-props & default-props]
  (let [default-props (apply map/deep-merge default-props)]
       (if (map/has-same-keys? default-props element-props)
           (map/deep-merge     default-props element-props)
           (-> element-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn force-values
  ; @description
  ; - Replaces values in the given 'element-props' map with corresponding values from the given 'forced-props' map(s).
  ; - Updates map type values in the given 'element-props' map by merging them with corresponding values from the given 'forced-props' map(s).
  ;
  ; @param (map) element-props
  ; @param (list of maps) forced-props
  ;
  ; @usage
  ; (force-values {:text-color :muted ...}
  ;               {:text-color :highlight
  ;                :width      :s})
  ; =>
  ; {:text-color :highlight
  ;  :width      :s
  ;  ...}
  ;
  ; @usage
  ; Multiple 'forced-props' maps can be provided:
  ; (force-values {...} ; <- The 'element-props' map is the SECONDARY source of values.
  ;               {...} ; <- The list of 'forced-props' maps is the PRIMARY source of values.
  ;               {...} ...)
  ;
  ; @return (map)
  [element-props & forced-props]
  (let [forced-props (apply map/deep-merge forced-props)]
       (map/deep-merge element-props forced-props)))

(defn force-value-group
  ; @description
  ; - Replaces values in the given 'element-props' map with corresponding values from the given 'forced-props' map(s)
  ;   in case at least one key of the given 'forced-props' map is present in the given 'element-props' map.
  ; - Updates map type values in the given 'element-props' map by merging them with corresponding values from the given 'forced-props' map(s)
  ;   in case at least one key of the given 'forced-props' map is present in the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (list of maps) forced-props
  ;
  ; @usage
  ; (force-value-group {:icon        :people ...}
  ;                    {:icon        :close
  ;                     :icon-family :material-icons-outlined
  ;                     :icon-size   :s})
  ; =>
  ; {:icon        :close
  ;  :icon-family :material-icons-outlined
  ;  :icon-size   :s
  ;  ...}
  ;
  ; @usage
  ; Multiple 'forced-props' maps can be provided:
  ; (force-value-group {...} ; <- The 'element-props' map is the PRIMARY source of values.
  ;                    {...} ; <- The list of 'forced-props' maps is the SECONDARY source of values.
  ;                    {...} ...)
  ;
  ; @return (map)
  [element-props & forced-props]
  (let [forced-props (apply map/deep-merge forced-props)]
       (if (map/has-same-keys? element-props forced-props)
           (map/deep-merge     element-props forced-props)
           (-> element-props))))
