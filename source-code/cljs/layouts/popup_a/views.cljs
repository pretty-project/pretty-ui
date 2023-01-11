
(ns layouts.popup-a.views
    (:require [layouts.popup-a.helpers    :as helpers]
              [layouts.popup-a.prototypes :as prototypes]
              [layouts.popup-a.state      :as state]
              [hiccup.api                 :as hiccup]
              [re-frame.api               :as r]
              [react.api                  :as react]
              [reagent.api                :as reagent]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:footer (metamorphic-content)}
  [popup-id {:keys [footer]}]
  [:div.l-popup-a--footer {:data-shadow-visible (popup-id @state/FOOTER-SHADOW-VISIBLE?)}
                          [:div.l-popup-a--footer-content [x.components/content popup-id footer]]])

(defn- footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:footer (metamorphic-content)}
  [popup-id {:keys [footer] :as layout-props}]
  (if footer (reagent/lifecycles {:component-did-mount    (fn [] (helpers/footer-did-mount-f    popup-id))
                                  :component-will-unmount (fn [] (helpers/footer-will-unmount-f popup-id))
                                  :reagent-render         (fn [] [footer-structure popup-id layout-props])})))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:body (metamorphic-content)
  ; :header (metamorphic-content)(opt)}
  [popup-id {:keys [body header]}]
  ; - A footer-sensor elemet az .l-popup-a--body-content elemben megjelenített tartalom végén szükséges
  ;  megjelenítetni!
  ;
  ; - Az .l-popup-a--body-content nem lehet alacsonyabb, mint a szülő eleme, hogy a benne megjelenített
  ;  tartalmat függőlegesen is középre lehessen igazítani!
  ;
  ; - Ha az .l-popup-a--body-content elem {height: 100%} vagy {flex-grow: 1} beállítással jelent meg,
  ;  akkor az elemben megjelenített túlméretes tartalom (ami magasabb mint a rendelkezésre álló hely)
  ;  kilógott (overflow) az .l-popup-a--body-content elemből, és ha a footer-sensor az .l-popup-a--body-content
  ;  elem után következett a DOM-fában, akkor nem a kilógó tartalom végén jelent meg, hanem a {height: 100%}
  ;  magas elem alatt (ami kisebb, mint a kilógó tartalma) és mivel a footer-sensor nem a tartalom
  ;  végén jelent meg ezért nem működött megfelelően.
  ;
  ; - Ha az .l-popup-a--body-content elem {min-height: 100%} beállítással jelent meg, akkor a benne
  ;  megjelenített tartalom nem tudta örökölni a magasságát, mivel a min-height tulajdonságból nem
  ;  örökölhető magasság!
  ;
  ; - A megoldás az, hogy a footer-sensor elemet az .l-popup-a--body-content elemen belül a tartalom
  ;  után kell elhelyezni!
  [:div.l-popup-a--body [:div.l-popup-a--body-content (if header [:div {:id (hiccup/value popup-id "header-sensor")}])
                                                      [x.components/content popup-id body]
                                                      (if footer [:div {:id (hiccup/value popup-id "footer-sensor")}])]])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:header (metamorphic-content)}
  [popup-id {:keys [header]}]
  [:div.l-popup-a--header {:data-shadow-visible (popup-id @state/HEADER-SHADOW-VISIBLE?)}
                          [:div.l-popup-a--header-content [x.components/content popup-id header]]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:header (metamorphic-content)(opt)}
  [popup-id {:keys [header] :as layout-props}]
  (if header (reagent/lifecycles {:component-did-mount    (fn [] (helpers/header-did-mount-f    popup-id))
                                  :component-will-unmount (fn [] (helpers/header-will-unmount-f popup-id))
                                  :reagent-render         (fn [] [header-structure popup-id layout-props])})))

(defn- layout-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:min-width (keyword)}
  [popup-id {:keys [min-width] :as layout-props}]
  [:div.l-popup-a--layout-structure {:data-content-min-width min-width}
                                    [:div.l-popup-a--layout-hack [body   popup-id layout-props]
                                                                 [header popup-id layout-props]]
                                    [footer popup-id layout-props]])

(defn- popup-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:close-by-cover? (boolean)(opt)}
  ;  :stretch-orientation (keyword)(opt)
  ;  :style (map)(opt)}
  [popup-id {:keys [close-by-cover? stretch-orientation style] :as layout-props}]
  [:div.l-popup-a {:data-stretch-orientation stretch-orientation
                   :style                    style}
                  [:div.l-popup-a--cover (if close-by-cover? {:on-click #(r/dispatch [:x.ui/remove-popup! popup-id])})]
                  [layout-structure popup-id layout-props]])

(defn layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:body (metamorphic-content)
  ;  :close-by-cover? (boolean)(opt)
  ;   Default: true
  ;  :footer (metamorphic-content)(opt)
  ;  :header (metamorphic-content)(opt)
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :none
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :stretch-orientation (keyword)(opt)
  ;  :horizontal, :vertical, :both, :none,
  ;   Default: :none
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [layout :my-popup {...}]
  [popup-id {:keys [on-mount on-unmount] :as layout-props}]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [popup-a popup-id layout-props])})))
