
(ns pretty-inputs.api
    (:require [pretty-inputs.combo-box.effects]
              [pretty-inputs.combo-box.events]
              [pretty-inputs.combo-box.side-effects]
              [pretty-inputs.counter.effects]
              [pretty-inputs.counter.events]
              [pretty-inputs.counter.subs]
              [pretty-inputs.input.events]
              [pretty-inputs.input.side-effects]
              [pretty-inputs.input.subs]
              [pretty-inputs.multi-combo-box.effects]
              [pretty-inputs.multi-combo-box.events]
              [pretty-inputs.multi-combo-box.side-effects]
              [pretty-inputs.multi-field.effects]
              [pretty-inputs.multi-field.events]
              [pretty-inputs.multi-field.subs]
              [pretty-inputs.slider.effects]
              [pretty-inputs.slider.events]
              [pretty-inputs.slider.subs]
              [pretty-inputs.checkbox.views        :as checkbox.views]
              [pretty-inputs.chip-group.views      :as chip-group.views]
              [pretty-inputs.combo-box.views       :as combo-box.views]
              [pretty-inputs.counter.views         :as counter.views]
              [pretty-inputs.date-field.views      :as date-field.views]
              [pretty-inputs.digit-field.views     :as digit-field.views]
              [pretty-inputs.multi-combo-box.views :as multi-combo-box.views]
              [pretty-inputs.multi-field.views     :as multi-field.views]
              [pretty-inputs.multiline-field.views :as multiline-field.views]
              [pretty-inputs.number-field.views    :as number-field.views]
              [pretty-inputs.password-field.views  :as password-field.views]
              [pretty-inputs.radio-button.views    :as radio-button.views]
              [pretty-inputs.search-field.views    :as search-field.views]
              [pretty-inputs.select.views          :as select.views]
              [pretty-inputs.slider.views          :as slider.views]
              [pretty-inputs.switch.views          :as switch.views]
              [pretty-inputs.text-field.views      :as text-field.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/input)
(def checkbox        checkbox.views/input)
(def chip-group      chip-group.views/input)
(def combo-box       combo-box.views/input)
(def counter         counter.views/input)
(def date-field      date-field.views/input)
(def digit-field     digit-field.views/input)
(def multi-combo-box multi-combo-box.views/input)
(def multi-field     multi-field.views/input)
(def multiline-field multiline-field.views/input)
(def number-field    number-field.views/input)
(def password-field  password-field.views/input)
(def radio-button    radio-button.views/input)
(def search-field    search-field.views/input)
(def select          select.views/input)
(def slider          slider.views/input)
(def switch          switch.views/input)
(def text-field      text-field.views/input)
