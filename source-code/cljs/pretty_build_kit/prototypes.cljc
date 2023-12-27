
(ns pretty-build-kit.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-values
  ; @note
  ; Use keyword type keys in both maps!
  ;
  ; @description
  ; - Replaces NIL or missing values in the given 'element-props' map with corresponding values from the given 'default-values' map.
  ; - Updates map type values in the given 'element-props' map by merging them with corresponding values from the given 'default-values' map.
  ;
  ; @param (map) element-props
  ; @param (map) default-values
  ;
  ; @usage
  ; (default-values {:text-color :muted}
  ;                 {:text-color :highlight
  ;                  :width      :s})
  ; =>
  ; {:text-color :muted
  ;  :width      :s}
  ;
  ; @return (map)
  [element-props default-values]
  (letfn [(f0 [result k v]
              (cond (-> result k nil?) (-> result (assoc k v))
                    (-> result k map?) (-> result (assoc k (merge (k result) v)))
                    :return result))]
         (reduce-kv f0 element-props default-values)))

(defn default-value-group
  ; @note
  ; Use keyword type keys in both maps!
  ;
  ; @description
  ; - Replaces NIL or missing values in the given 'element-props' map with corresponding values from the given 'default-value-group' map
  ;   in case at least one key of the given 'default-value-group' map is present in the given 'element-props' map.
  ; - Updates map type values in the given 'element-props' map by merging them with corresponding values from the given 'default-value-group' map
  ;   in case at least one key of the given 'default-value-group' map is present in the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map) default-value-group
  ;
  ; @usage
  ; (default-value-group {:icon        :people}
  ;                      {:icon        :close
  ;                       :icon-family :material-icons-outlined
  ;                       :icon-size   :s})
  ; =>
  ; {:icon        :people
  ;  :icon-family :material-icons-outlined
  ;  :icon-size   :s}
  ;
  ; @return (map)
  [element-props default-value-group]
  (letfn [(f0 [[k v]]
              (if (-> element-props k some?)
                  (-> element-props (default-values default-value-group))))]
         (or (some f0 default-value-group)
             (-> element-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn forced-values
  ; @note
  ; Use keyword type keys in both maps!
  ;
  ; @description
  ; - Replaces values in the given 'element-props' map with corresponding values from the given 'forced-values' map.
  ; - Updates map type values in the given 'element-props' map by merging them with corresponding values from the given 'forced-values' map.
  ;
  ; @param (map) element-props
  ; @param (map) forced-values
  ;
  ; @usage
  ; (forced-values {:text-color :muted}
  ;                {:text-color :highlight
  ;                 :width      :s})
  ; =>
  ; {:text-color :highlight
  ;  :width      :s}
  ;
  ; @return (map)
  [element-props forced-values]
  (letfn [(f0 [result k v]
              (if (-> result k map?)
                  (-> result (assoc k (merge (k result) v)))
                  (-> result (assoc k v))))]
         (reduce-kv f0 element-props forced-values)))

(defn forced-value-group
  ; @note
  ; Use keyword type keys in both maps!
  ;
  ; @description
  ; - Replaces values in the given 'element-props' map with corresponding values from the given 'forced-value-group' map
  ;   in case at least one key of the given 'forced-value-group' map is present in the given 'element-props' map.
  ; - Updates map type values in the given 'element-props' map by merging them with corresponding values from the given 'forced-value-group' map
  ;   in case at least one key of the given 'forced-value-group' map is present in the given 'element-props' map.
  ;
  ; @param (map) element-props
  ; @param (map) forced-value-group
  ;
  ; @usage
  ; (forced-value-group {:icon        :people}
  ;                     {:icon        :close
  ;                      :icon-family :material-icons-outlined
  ;                      :icon-size   :s})
  ; =>
  ; {:icon        :close
  ;  :icon-family :material-icons-outlined
  ;  :icon-size   :s}
  ;
  ; @return (map)
  [element-props forced-value-group]
  (letfn [(f0 [[k v]]
              (if (-> element-props k some?)
                  (-> element-props (forced-values forced-value-group))))]
         (or (some f0 forced-value-group)
             (-> element-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-update-fns
  ; @note
  ; Use keyword type keys in both maps!
  ;
  ; @description
  ; Updates values in the given 'element-props' map with functions from the given 'value-update-fns' map
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

(defn value-wrap-fns
  ; @note
  ; Use keyword type keys in both maps!
  ;
  ; @description
  ; Wraps values in the given 'element-props' map with functions from the given 'value-wrap-fns' map
  ; in case their keys are present in both maps.
  ;
  ; @param (map) element-props
  ; @param (map) value-wrap-fns
  ;
  ; @usage
  ; (value-wrap-fns {:on-click      "Hello World!"}
  ;                 {:on-click      println
  ;                  :on-mouse-over println})
  ; =>
  ; {:on-click #(println "Hello World!")}
  ;
  ; @return (map)
  [element-props value-update-fns]
  (letfn [(f0 [result k v]
              (if-let [x (k element-props)]
                      (-> result (assoc k #(v x)))
                      (-> result)))]
         (reduce-kv f0 element-props value-update-fns)))
