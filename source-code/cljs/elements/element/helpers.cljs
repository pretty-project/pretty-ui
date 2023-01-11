
(ns elements.element.helpers
    (:require [candy.api        :refer [param return]]
              [css.api          :as css]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-preset
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) presets
  ; @param (map) element-props
  ; {:preset (keyword)(opt)}
  ;
  ; @usage
  ; (apply-preset {:my-preset {...}}
  ;               {:preset :my-preset ...})
  ;
  ; @example
  ; (apply-preset {:my-preset {:hover-color :highlight}}
  ;               {:preset :my-preset})
  ; =>
  ; {:hover-color :highlight
  ;  :preset      :my-preset}
  ;
  ; @return (map)
  [presets {:keys [preset] :as element-props}]
  (if preset (let [preset-props (get presets preset)]
                  (merge preset-props element-props))
             (return element-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-color
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-attributes
  ; {:style (map)(opt)}
  ; @param (keyword) color-key
  ; @param (keyword) color-data-key
  ; @param (keyword or string) color-value
  ;
  ; @example
  ; (apply-color {...} :color :data-color :muted)
  ; =>
  ; {:data-color :muted}
  ;
  ; @example
  ; (apply-color {...} :color :data-color "#fff")
  ; =>
  ; {:data-color :var :style {"--color" "fff"}}
  ;
  ; @example
  ; (apply-color {:style {:padding "12px"}} :color :data-color "#fff")
  ; =>
  ; {:data-color :var :style {"--color" "fff" :padding "12px"}}
  ;
  ; @return (map)
  ; {:style (map)}
  [element-attributes color-key color-data-key color-value]
  (cond (keyword? color-value) (-> element-attributes (assoc-in [color-data-key]                 color-value))
        (string?  color-value) (-> element-attributes (assoc-in [:style (css/var-key color-key)] color-value)
                                                      (assoc-in [color-data-key]                 :var))
        :return element-attributes))

(defn apply-dimension
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-attributes
  ; @param (keyword) dimension-key
  ; @param (keyword) dimension-data-key
  ; @param (keyword, px or string) dimension-value
  ;
  ; @example
  ; (apply-dimension {...} :width :data-block-width 42)
  ; =>
  ; {:style {:width "42px"}}
  ;
  ; @example
  ; (apply-dimension {...} :width :data-block-width "42%")
  ; =>
  ; {:style {:width "42%"}}
  ;
  ; @example
  ; (apply-dimension {...} :width :data-block-width :s)
  ; =>
  ; {:data-block-width :s}
  ;
  ; @return (map)
  [element-attributes dimension-key dimension-data-key dimension-value]
  (cond (keyword? dimension-value) (assoc    element-attributes dimension-data-key dimension-value)
        (integer? dimension-value) (assoc-in element-attributes [:style dimension-key] (css/px dimension-value))
        (string?  dimension-value) (assoc-in element-attributes [:style dimension-key] (param  dimension-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-badge-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:badge-color (keyword)
  ;  :badge-content (metamorphic-content)
  ;  :badge-position (keyword)}
  ;
  ; @return (map)
  ; {:data-badge-color (keyword)
  ;  :data-badge-content (string)
  ;  :data-badge-position (keyword)}
  [_ {:keys [badge-color badge-content badge-position]}]
  {:data-badge-content  (x.components/content badge-content)
   :data-badge-color    badge-color
   :data-badge-position badge-position})

(defn element-marker-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:marker-color (keyword)
  ;  :marker-position (keyword)}
  ;
  ; @return (map)
  ; {:data-marker-color (keyword)
  ;  :data-marker-position (keyword)}
  [_ {:keys [marker-color marker-position]}]
  {:data-marker-color    marker-color
   :data-marker-position marker-position})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-default-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-disabled (boolean)
  ;  :id (string)
  ;  :key (string)
  ;  :style (map)}
  [element-id {:keys [class disabled? style]}]
  ; BUG#4044
  ; Ha egy listában a listaelemek toggle elemet tartalmaznak és ...
  ; ... a toggle elem nem kap egyedi azonosítót, mert ugyanaz az azonosító ismétlődne
  ;     az összes listaelem toggle elemében,
  ; ... a toggle elem {:hover-color ...} tulajdonsággal rendelkezik,
  ; ... az element-default-attributes függvény React kulcsként alkalmazza az elemek
  ;     azonosítóját,
  ; ... az egyes listaelemekre kattintva olyan változás történik (pl. kijelölés),
  ;     ami miatt az adott listaelem paraméterezése megváltozik,
  ; akkor az egyes listaelemekre kattintva ...
  ; ... a megváltozó paraméterek miatt a listaelem újrarenderelődik,
  ; ... a listaelem toggle eleme is újrarenderelődik, ami miatt új azonosítót kap,
  ; ... a toggle elem az új azonosítója miatt új React kulcsot kap,
  ; ... a toggle elem az új React kulcs beállításának pillanatában másik React-elemmé
  ;     változik és a váltás közben Ca. 15ms ideig nem látszódik a {:hover-color ...}
  ;     tulajdonság színe (rövid villanásnak tűnik)
  ;
  ; XXX#4004
  ; Az x4.7.6 verzióig egyetlen esetben sem volt rá szükség, hogy egy element rendelkezzen
  ; DOM azonosítóval.
  ; + Talán könnyebb a böngészőnek, ha kevesebb az azonosítóval rendelkező elem ...
  {:class         class
   :data-disabled disabled?})
  ;:id  (hiccup/value element-id)
  ;:key (hiccup/value element-id)

(defn element-indent-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:indent (map)(opt)}
  ;
  ; @return (map)
  [_ {:keys [indent]}]
  (letfn [(f [result key value]
             (assoc result (keyword (str "data-indent-" (name key))) value))]
         (reduce-kv f {} indent)))

(defn element-outdent-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:outdent (map)(opt)}
  ;
  ; @return (map)
  [_ {:keys [outdent]}]
  (letfn [(f [result key value]
             (assoc result (keyword (str "data-outdent-" (name key))) value))]
         (reduce-kv f {} outdent)))
