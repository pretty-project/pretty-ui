
(ns pretty-defaults.utils
    (:require [fruits.map.api :as map]
              [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-default-values
  ; @note
  ; Use keyword type keys!
  ;
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
  ; @note
  ; Use keyword type keys!
  ;
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
  ; @note
  ; Use keyword type keys!
  ;
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
  ; @note
  ; Use keyword type keys!
  ;
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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------




; deprecated
(defn value-update-fns
  ; @note
  ; Use keyword type keys!
  ;
  ; @description
  ; Updates values within the given 'element-props' map with functions from the given 'value-update-fns' map
  ; in case their keys are present in both maps.
  ;
  ; @param (map) element-props
  ; @param (map) value-update-fns
  ;
  ; @usage
  ; (value-update-fns {:content     "My content"}
  ;                   {:content     clojure.string/upper-case
  ;                    :placeholder clojure.string/upper-case})
  ; =>
  ; {:content "MY CONTENT"}
  ;
  ; @return (map)
  [element-props value-update-fns]
  (letfn [(f0 [result k v]
              (if (-> element-props k some?)
                  (-> result (update k v))
                  (-> result)))]
         (reduce-kv f0 element-props value-update-fns)))
; deprecated
