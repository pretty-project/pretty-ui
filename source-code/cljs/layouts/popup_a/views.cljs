
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-a.views
    (:require [layouts.popup-a.helpers    :as helpers]
              [layouts.popup-a.prototypes :as prototypes]
              [layouts.popup-a.state      :as state]
              [react.api                  :as react]
              [reagent.api                :as reagent]
              [x.app-components.api       :as components]
              [x.app-core.api             :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:footer (metamorphic-content)}
  [popup-id {:keys [footer]}]
  [:div.popup-a--footer {:data-shadow-visible (popup-id @state/FOOTER-SHADOW-VISIBLE?)}
                        [:div.popup-a--footer-content [components/content popup-id footer]]])

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
  ;  :header (metamorphic-content)(opt)}
  [popup-id {:keys [body header]}]
  ; - A footer-sensor elemet a .popup-a--body-content elemben megjelenített tartalom végén szükséges
  ;   megjelenítetni!
  ;
  ; - A .popup-a--body-content nem lehet alacsonyabb, mint a szülő eleme, hogy a benne megjelenített
  ;   tartalmat függőlegesen is középre lehessen igazítani!
  ;
  ; - Ha a .popup-a--body-content elem {height: 100%} vagy {flex-grow: 1} beállítással jelent meg,
  ;   akkor az elemben megjelenített túlméretes tartalom (ami magasabb mint a rendelkezésre álló hely)
  ;   kilógott (overflow) a .popup-a--body-content elemből, és ha a footer-sensor a .popup-a--body-content
  ;   elem után következett a DOM-fában, akkor nem a kilógó tartalom végén jelent meg, hanem a {height: 100%}
  ;   magas elem alatt (ami kisebb, mint a kilógó tartalma) és mivel a footer-sensor nem a tartalom
  ;   végén jelent meg ezért nem működött megfelelően.
  ;
  ; - Ha a .popup-a--body-content elem {min-height: 100%} beállítással jelent meg, akkor a benne
  ;   megjelenített tartalom nem tudta örökölni a magasságát, mivel a min-height tulajdonságból nem
  ;   örökölhető magasság!
  ;
  ; - A megoldás az, hogy a footer-sensor elemet a .popup-a--body-content elemen belül a tartalom
  ;   után kell elhelyezni!
  [:div.popup-a--body [:div.popup-a--body-content (if header [:div {:id (a/dom-value popup-id "header-sensor")}])
                                                  [components/content popup-id body]
                                                  (if footer [:div {:id (a/dom-value popup-id "footer-sensor")}])]])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:header (metamorphic-content)}
  [popup-id {:keys [header]}]
  [:div.popup-a--header {:data-shadow-visible (popup-id @state/HEADER-SHADOW-VISIBLE?)}
                        [:div.popup-a--header-content [components/content popup-id header]]])

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
  [popup-id layout-props]
  [:div.popup-a--layout-structure [:div.popup-a--layout-hack [body   popup-id layout-props]
                                                             [header popup-id layout-props]]
                                  [footer popup-id layout-props]])

(defn- popup-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ;  {:close-by-cover? (boolean)(opt)}
  [popup-id {:keys [close-by-cover?] :as layout-props}]
  [:div.popup-a (helpers/layout-attributes popup-id layout-props)
                [:div.popup-a--cover (if close-by-cover? {:on-click #(a/dispatch [:ui/close-popup! popup-id])})]
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
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   Default: :none
  ;  :stretch-orientation (keyword)(opt)
  ;   :horizontal, :vertical, :both, :none,
  ;   Default: :none
  ;  :style (map)(opt)}
  ;
  ; @usage
  ;  [popup-a/layout :my-popup {...}]
  [popup-id layout-props]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       [popup-a popup-id layout-props]))