
(ns pretty-presets.default-presets.presets
    (:require [pretty-presets.preset-pool.side-effects :refer [reg-preset!]]))

;; -- Popup menu buttons ------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-preset! :default-presets/popup-menu-button
             {:border-radius    {:all :s}
              :fill-color       :default
              :font-size        :xs
              :gap              :xs
              :horizontal-align :left
              :hover-color      :default
              :icon-color       :default
              :icon-size        :m
              :indent           {:horizontal :xxs :vertical :xxs}
              :width            :auto})

(reg-preset! :default-presets/popup-menu-button.highlight
             {:fill-color   :highlight
              :hover-color  :highlight
              :preset       :default-presets/popup-menu-button})

(reg-preset! :default-presets/popup-menu-button.muted
             {:fill-color   :muted
              :hover-color  :muted
              :preset       :default-presets/popup-menu-button})

(reg-preset! :default-presets/popup-menu-button.primary
             {:fill-color   :primary
              :hover-color  :primary
              :preset       :default-presets/popup-menu-button})

(reg-preset! :default-presets/popup-menu-button.secondary
             {:fill-color   :secondary
              :hover-color  :secondary
              :preset       :default-presets/popup-menu-button})

(reg-preset! :default-presets/popup-menu-button.success
             {:fill-color   :success
              :hover-color  :success
              :preset       :default-presets/popup-menu-button})

(reg-preset! :default-presets/popup-menu-button.warning
             {:fill-color   :warning
              :hover-color  :warning
              :preset       :default-presets/popup-menu-button})

;; -- Popup menu labels -------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-preset! :default-presets/popup-menu-label
             {:border-color     :default
              :border-position  :bottom
              :border-width     :xs
              :font-weight      :semi-bold
              :gap              :auto
              :icon-position    :right
              :icon-size        :xl
              :indent           {:bottom :xxs}
              :text-transform   :uppercase
              :width            :auto})

;; -- Side menu buttons -------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-preset! :default-presets/side-menu-button
             {:font-size        :xs
              :gap              :xs
              :horizontal-align :left
              :hover-color      :highlight
              :icon-size        :m
              :indent           {:horizontal :xs :left :s :right :xl}})

(reg-preset! :default-presets/side-menu-button.active
             {:fill-color  :muted
              :font-weight :semi-bold
              :hover-color :muted
              :preset      :default-presets/side-menu-button})

;; -- Sidebar buttons ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-preset! :default-presets/sidebar-button
             {:color            :invert
              :font-size        :xs
              :font-weight      :medium
              :gap              :xs
              :horizontal-align :left
              :hover-color      :invert
              :icon-size        :m
              :indent           {:left :s :right :xl :horizontal :xs}})
